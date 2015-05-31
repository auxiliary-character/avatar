(ns avatar.core
  (:import [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [java.io File])
  (:use clojure.pprint)
  (:gen-class))

(defn write-png!
  [content filename]
  (let [xlen (count content)
        ylen (count (first content))
        bi (BufferedImage. xlen ylen BufferedImage/TYPE_INT_RGB)]
    (doseq [x (range xlen)
            y (range ylen)]
      (let [line (vec (nth content (- xlen (inc x))))
            pix (nth line (- ylen (inc y)))]
        (if pix
          (.setRGB bi x y 0x000000)
          (.setRGB bi x y 0xFFFFFF))))
    (ImageIO/write bi "png" (File. filename))))

(defn xor
  "Apparently clojure doesn't come with an xor function. Maybe I'm wrong?"
  [l r]
  (or
    (and l (not r))
    (and (not l) r)))

(defn sierpinski-step
  [prev]
  (into (empty prev)
        (for [i (range (count prev))]
          (if (= i 0)
            true
            (xor (nth prev i) (nth prev (dec i)))))))

(defn sierpinski-square
  [len]
  (loop [lines [(vec (concat [true] (repeat (dec len) false)))]
         i (- len 2)]
    (let [prev-line (last lines)
          new-line (sierpinski-step prev-line)
          new-lines (conj lines new-line)]
      (if (= i 0)
        new-lines
        (recur new-lines (dec i))))))

(defn pad-1d
  [border-width fill-width padding content]
  (into (empty content)
        (apply concat
               (for [i content]
                 (into (empty content)
                       (concat (repeat border-width padding)
                               (repeat fill-width i)
                               (repeat border-width padding)))))))

(defn pad-2d
  [border-width fill-width padding content]
  (let [xlen (count content)
        ylen (count (first content))

        empty-line (pad-1d border-width fill-width padding
                           (into (empty content) (repeat ylen padding)))]
    (pad-1d border-width fill-width empty-line
            (map (partial pad-1d border-width fill-width padding)
                 content))))

(defn make-it! [size filename]
  (let [data (sierpinski-square size)
        padded-data (pad-2d 3 30 false data)]
    (write-png! padded-data filename)))

(defn -main
  [& args]
  (doseq [size [4 8 16 32 64 128]]
    (let [filename (format "avatar-%02d.png" size)]
      (make-it! size filename))))
