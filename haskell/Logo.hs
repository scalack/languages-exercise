-----------------------------------------------------------------------------
-- | Logo.hs
-- Module : Logo
-- Copyright : (c) 2011 Boyun Tang
-- License : BSD-style
-- Maintainer : tangboyun@hotmail.com
-- Stability : experimental
-- Portability : ghc
-- �򵥵�Logo��ͼ����
-- 
--
-----------------------------------------------------------------------------
module Logo
       (
         Logo
        ,Turtle
        ,initTurtle
        ,move
        ,forward
        ,turn
        ,resize
        ,recDraw
        ,runLogo
       )
       where
import Control.Monad
import Control.Arrow
import Control.Monad.Writer
import Graphics.Rendering.Cairo

type Vec = (Double,Double)
type Point = (Double,Double)

newtype Turtle = T {
  runT :: (Point,Vec)
  }

type Logo = Writer [Render ()] Turtle

initTurtle :: Double -> Double -> Logo
initTurtle x y = writer (T ( (x,y),(1,0.0) ),[moveTo x y])

-- | �Ƕ��뻡�ȵ�ת��  
angToPi :: Double -> Double
angToPi a = 2.0 * pi * a / 360.0 

-- | �ƶ���������
move :: Double -> Turtle -> Logo
move d (T ( (x,y),w@(u,v) ) ) =
  let x' = x+d*u
      y' = y+d*v
  in writer (T ( (x',y'), w),[moveTo x' y'])

-- | ת����ʱ��Ϊ��
turn :: Double -> Turtle -> Logo
turn a (T (p,(u,v) ) )  =
    let a' = angToPi (-a)
   
    in   writer (T (p,(u*cos a' - v*sin a', u*sin a' + v*cos a') ),[])



-- | ���Ըı�֮���·����С

resize :: Double -> Turtle -> Logo
resize s (T (p,w) ) = writer (T (p, join (***) (s *) w),[])

-- | �ƶ��һ���
forward :: Double -> Turtle -> Logo
forward d (T ( (x,y),w@(u,v) ) ) =
  let x' = x+d*u
      y' = y+d*v
  in writer (T ( (x',y'), w),[lineTo x' y'])

-- | �򵥵ĵݹ���ƽӿ�
recDraw :: (Turtle -> Logo) -- ^ �ݹ���û��ƵĻ���ͼ��
        -> (Turtle -> Logo) -- ^ ÿ�ε���֮��ĺ������
        -> Int        -- ^ �ݹ����
        -> Turtle          -- ^ ��ʼ��
        -> Logo
recDraw drawFunc afterDraw n t = go n t
  where
    go n | n > 0 = \input -> drawFunc input >>= afterDraw >>= go (n - 1)
         | otherwise = return 

-- | ��ȡMonad��˳��ִ��cairo���Ʋ�����
runLogo :: Logo -> Render ()
runLogo = sequence_ . execWriter
-----------------------------------------------------------------------------
-- | Toy.hs
-- Module : Main
-- Copyright : (c) 2011 Boyun Tang
-- License : BSD-style
-- Maintainer : tangboyun@hotmail.com
-- Stability : experimental
-- Portability : ghc
--
-- 
--
-----------------------------------------------------------------------------
module Main where
import Logo
import Graphics.Rendering.Cairo
import Control.Monad

main :: IO ()
main = do
  sur <- createImageSurface FormatARGB32 600 400 -- 600 �� 400 ����
  renderWith sur myPic
  surfaceWriteToPNG sur "Star.png" 

-- | �����ֱ�Ϊת�ǡ��߳�����ʼ��
drawStar :: Double -> Double -> Turtle -> Logo
drawStar a d i = go 5 i
  where
    go time | time > 0 = \i -> forward d i >>= turn a >>= go (time - 1)
            | otherwise = return

-- | ���ߣ��ݹ�ػ����ǣ�ÿ����һ���Ժ�תa�ȣ���������s�����ݹ�n��
spiral :: Double -> Double -> Double -> Int -> Turtle -> Logo
spiral s a d n =
  turn (-a) >=>
  recDraw (drawStar (-144.0) d)
          (turn  a >=> resize s) n

myPic = do
  setSourceRGB 0 0 0  -- ��ɫ
  setLineWidth 1.2    -- �Ӵ�1.2��
  runLogo $ initTurtle 200 180 >>= spiral 0.9 (-18) 300 40 -- ��ʼ���꣨200,180��
  stroke