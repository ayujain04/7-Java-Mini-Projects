

/**
 * Celestial Body class for NBody
 * Modified from original Planet class
 * used at Princeton and Berkeley
 * @author ola
 *
 * If you add code here, add yourself as @author below
 *
 *
 */
public class CelestialBody {

	private double myXPos;
	private double myYPos;
	private double myXVel;
	private double myYVel;
	private double myMass;
	private String myFileName;

	/**
	 * Create a Body from parameters	
	 * @param xp initial x position
	 * @param yp initial y position
	 * @param xv initial x velocity
	 * @param yv initial y velocity
	 * @param mass of object
	 * @param filename of image for object animation
	 */
	public CelestialBody(double xp, double yp, double xv,
			             double yv, double mass, String filename){
							myXPos = xp;
							 myYPos = yp;
							 myXVel = xv;
							 myYVel = yv;
							 myMass = mass;
							 myFileName = filename;
	}

	/**
	 *
	 * @return
	 */
	public double getX() {
		// TODO: complete method
		return myXPos;
	}

	/**
	 *
	 * @return
	 */
	public double getY() {
		// TODO: complete method
		return myYPos;
	}

	/**
	 * Accessor for the x-velocity
	 * @return the value of this object's x-velocity
	 */
	public double getXVel() {
		// TODO: complete method
		return myXVel;
	}
	/**
	 * Return y-velocity of this Body.
	 * @return value of y-velocity.
	 */
	public double getYVel() {
		// TODO: complete method
		return myYVel;
	}

	/**
	 *
	 * @return
	 */
	public double getMass() {
		// TODO: complete method
		return myMass;
	}

	/**
	 *
	 * @return
	 */
	public String getName() {
		// TODO: complete method
		return myFileName;
	}

	/**
	 * Return the distance between this body and another
	 * @param b the other body to which distance is calculated
	 * @return distance between this body and b
	 */
	public double calcDistance(CelestialBody b) {
		// TODO: complete method
		double distance = Math.sqrt(((this.myXPos-b.getX())*(this.myXPos-b.getX())) +  (this.myYPos-b.getY())*(this.myYPos-b.getY())); 
		return distance;
	}

	public double calcForceExertedBy(CelestialBody b) {
		// TODO: complete method
		double force = ((6.67*1e-11)*this.myMass*b.getMass())/((this.calcDistance(b))*(this.calcDistance(b))); 
		return force;
	}

	public double calcForceExertedByX(CelestialBody b) {
		// TODO: complete method
		double forceX = (this.calcForceExertedBy(b)*(  (b.getX()-this.myXPos)/ (this.calcDistance(b))));
		return forceX;
	}
	public double calcForceExertedByY(CelestialBody b) {
		// TODO: complete method
		double forceY = (this.calcForceExertedBy(b)*(  (b.getY()-this.myYPos)/ (this.calcDistance(b))));
		return forceY;
	}

	public double calcNetForceExertedByX(CelestialBody[] bodies) {
		// TODO: complete method
		double sum = 0.0;
		for(CelestialBody b : bodies){
			if(!b.equals(this)){
				sum+=this.calcForceExertedByX(b); 
			}
		}
	
		return sum;
	}

	public double calcNetForceExertedByY(CelestialBody[] bodies) {
		double sum = 0.0;
		for(CelestialBody b : bodies){
			if(!b.equals(this)){
				sum+=this.calcForceExertedByY(b); 
			}
		}
	
		return sum;
	}

	public void update(double deltaT, 
			           double xforce, double yforce) {
		// TODO: complete method
		double accX = xforce/this.myMass; 
		double accY = yforce/this.myMass; 
		myXVel += deltaT*accX; 
		myYVel += deltaT*accY; 
		myXPos += deltaT*myXVel; 
		myYPos += deltaT*myYVel; 

	}

	/**
	 * Draws this planet's image at its current position
	 */
	public void draw() {
		StdDraw.picture(myXPos,myYPos,"images/"+myFileName);
	}
}
