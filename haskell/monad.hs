module Mon where
type Name = String
data Expr = Num Int | Neg Expr | Add Expr Expr | Var Name | Let Expr Expr Expr

newtype Reader e a = Reader { runReader :: (e -> a) }

instance Monad (Reader e) where
    return a         = Reader $ \e -> a
    (Reader r) >>= f = Reader $ \e -> runReader (f (r e)) e


class MonadReader  m e | e -> m where
    ask   :: m e
    local :: (e -> e) -> m a -> m a 
{-
ghci -XMultiParamTypeClasses -XFunctionalDependencies -X
FlexibleInstances
-}

instance MonadReader (Reader e) e  where
    ask       = Reader id
    local f c = Reader $ \e -> runReader c (f e) 
    
reader :: Reader String Int

reader =  do 
      x <- Reader $ \e -> length(e)
      return x


say x  = 
	case x of
         'a' | on -> print 'a'   
         'b' | on ->  print 'b'   
         'c'      ->  print 'c'   
         _        -> print '.'   

	where on = True 