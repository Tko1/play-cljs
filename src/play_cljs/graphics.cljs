(ns play-cljs.graphics
  (:require [cljsjs.pixi]
            [play-cljs.core :as c]))

(defmulti draw-graphics! (fn [command _ _ _] (first command)))

(defmethod draw-graphics! :fill [command origin-x origin-y graphics]
  (let [[_ opts & children] command
        {:keys [color alpha]} opts]
    (.beginFill graphics color alpha)
    (draw-graphics! children origin-x origin-y graphics)
    (.endFill graphics)))

(defmethod draw-graphics! :circle [command origin-x origin-y graphics]
  (let [[_ opts & children] command
        {:keys [x y radius]} opts
        x (+ origin-x x)
        y (+ origin-y y)]
    (.drawCircle graphics x y radius)
    (draw-graphics! children x y graphics)))

(defmethod draw-graphics! :ellipse [command origin-x origin-y graphics]
  (let [[_ opts & children] command
        {:keys [x y width height]} opts
        x (+ origin-x x)
        y (+ origin-y y)]
    (.drawEllipse graphics x y width height)
    (draw-graphics! children x y graphics)))

(defmethod draw-graphics! :polygon [command origin-x origin-y graphics]
  (let [[_ opts & children] command
        {:keys [path]} opts
        path (->> path
                  (partition 2)
                  (map (fn [[x y]] [(+ origin-x x) (+ origin-y y)]))
                  flatten)]
    (.drawPolygon graphics (into-array path))
    (draw-graphics! children origin-x origin-y graphics)))

(defmethod draw-graphics! :rect [command origin-x origin-y graphics]
  (let [[_ opts & children] command
        {:keys [x y width height radius]} opts
        x (+ origin-x x)
        y (+ origin-y y)]
    (.drawRect graphics x y width height)
    (draw-graphics! children x y graphics)))

(defmethod draw-graphics! :rounded-rect [command origin-x origin-y graphics]
  (let [[_ opts & children] command
        {:keys [x y width height radius]} opts
        x (+ origin-x x)
        y (+ origin-y y)]
    (.drawRoundedRect graphics x y width height radius)
    (draw-graphics! children x y graphics)))

(defmethod draw-graphics! :default [command origin-x origin-y graphics]
  (cond
    (sequential? (first command))
    (run! #(draw-graphics! % origin-x origin-y graphics) command)
    (nil? (first command))
    nil
    :else
    (throw (js/Error. (str "Invalid graphics command: " (pr-str command))))))

(defrecord Graphics [command x y] c/Command
  (run [this game]
    (let [renderer (c/get-renderer game)
          graphics (js/PIXI.Graphics.)]
      (draw-graphics! command x y graphics)
      (.render renderer graphics))))

(defn graphics
  ([command]
   (graphics command 0 0))
  ([command x y]
   (Graphics. command x y)))
