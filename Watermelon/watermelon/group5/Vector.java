package watermelon.group5;

public class Vector {
    public double x;
    public double y;

    public Vector() {
    	this.x = 0;
    	this.y = 0;
    }
    
    public Vector(double x, double y) {
    	this.x = x;
    	this.y = y;
    }
    
    public Vector(Vector v) {
    	this.x = v.x;
    	this.y = v.y;
    }
    
    public Vector add(double x, double y) {
    	this.x += x;
    	this.y += y;
    	
    	return this;
    }
    
    public Vector add(Vector v) {
    	return add(v.x, v.y);
    }
    
    public Vector negate() {
    	x = -x;
    	y = -y;
    	
    	return this;
    }
    
    public boolean isNone() {
    	return Math.abs(this.x) <= 0.0000001 && Math.abs(this.y) <= 0.0000001;
    }
}