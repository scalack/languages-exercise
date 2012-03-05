module Main where
import Geroy 
import qualified Data.Set as Set 

tex = "jac head scala clojure"
set1 = Set.fromList  []
-- 这个函数很精彩的说~
explode [x] 0 = (x, [])
explode (x:xs) n
	| n == 0 = (x, xs)
	| otherwise = (a, x:b) where (a, b) = explode xs (n-1)

rand [] _ = []
rand ls g = x : (rand xs gen)
	where (x, xs) = ls `explode` i
		  (i, gen) = randomR (0::Int, (length ls) - 1) g



main = do 
       print(sp 2)
       print set1
