module StateGame where
 
import Control.Monad.State
 
-- Example use of State monad
-- Passes a string of dictionary {a,b,c}
-- Game is to produce a number from the string.
-- By default the game is off, a C toggles the
-- game on and off. A 'a' gives +1 and a b gives -1.
-- E.g 
-- 'ab'    = 0
-- 'ca'    = 1
-- 'cabca' = 0
-- State = game is on or off & current score
--       = (Bool, Int)
 
type GameValue = Int
type GameState = (Bool, Int)
newtype Any = Any { getAny :: Bool } deriving(Show)
newtype N = N Int deriving(Show)
data Foo1 = Foo1 Int  

 
playGame :: String -> State GameState GameValue
playGame []     = do
    (_, score) <- get
    return score


 
playGame (x:xs) = do
    (on, score) <- get
    case x of
         'a' | on ->  put (on, score + 1) 
         'b' | on -> put (on, score - 1)
         'c'      -> put (not on, score)
         _        -> put (on, score)
    
    
    playGame xs
    
startState = (False, 0)

y1 x = case x of
     Foo1 _ -> 1		-- undefined
 

y3 x  = case x  of
     N _ -> 1		 
      
main = do 
       print $ evalState (playGame "abcaaacbbcabbab") startState 
       print $ Any . getAny $ undefined