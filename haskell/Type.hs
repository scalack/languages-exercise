module Type where
import Types

data TrafficLight = Red | Yellow | Green 

instance Show TrafficLight where  
    show Red = "Red light"  
    show Yellow = "Yellow light"  
    show Green = "Green light"

Vec i j k `vplus` Vec a b c = Vec (i+a) (j+b) (k+c)
vc =  (Vec 1 2 3 `vplus` Vec 5 6 7)

 

main = 
		--print (Circle 10 20 10)
		print vc
		

