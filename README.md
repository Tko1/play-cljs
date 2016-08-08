## Introduction

A ClojureScript library for making games. It uses [p5.js](http://p5js.org/) underneath. You can create a new play-cljs project with the template:

```
boot -d seancorfield/boot-new new -t "play-cljs" -n "hello-world"
```

## Documentation

* Read the source (I know that sucks, but I am in very early stages of development!)
* Check out [the example games](https://github.com/oakes/play-cljs-examples)
* Join the discussion on [r/playcljs](https://www.reddit.com/r/playcljs/)
* Look at this commented example:

```clojure
(ns hello-world.core
  (:require [play-cljs.core :as p]))

(declare game)

; define a screen, where all the action takes place
(def main-screen
  (reify p/Screen
    ; all screen functions get a map called "state" that you can store anything inside of
    
    ; runs when the screen is first shown
    (on-show [this state]
      ; we use `set-state` to update the state map with the x and y position
      ; of the text we want to display
      (p/set-state game
        (assoc state :text-x 20 :text-y 30)))

    ; runs when the screen is hidden
    (on-hide [this state])

    ; runs every time a frame must be drawn (about 60 times per sec)
    (on-render [this state]
      ; we use `render` to display a light blue background and black text
      ; as you can see, everything is specified as a hiccup-style data structure
      (p/render game
        [[:fill {:color "lightblue"}
          [:rect {:x 0 :y 0 :width 500 :height 500}]]
         [:fill {:color "black"}
          ["Hello, world!" {:x (:text-x state) :y (:text-y state) :size 16 :font "Georgia" :style :italic}]]])
      ; we use `set-state` to increment the x position of the text so it scrolls to the right
      (p/set-state game
        (update state :text-x inc)))

    ; runs whenever an event you subscribed to happens (see below)
    (on-event [this state event])))

(defonce game (p/create-game 500 500))

(doto game
  (p/stop)
  (p/start ["keydown"])
  (p/set-screen main-screen))
```

## Licensing

All files that originate from this project are dedicated to the public domain. I would love pull requests, and will assume that they are also dedicated to the public domain.
