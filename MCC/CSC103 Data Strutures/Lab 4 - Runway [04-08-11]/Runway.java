public class Runway {
    public Runway (double _landingTime, double _takeoffTime, int _landingWaitLimit) {
        landingTime = _landingTime;
        takeoffTime = _takeoffTime;
        landingWaitLimit = _landingWaitLimit;
        
        currentTime = 0;
        currentPlane = null;
        
        planesLanded = 0;
        planesTakenOff = 0;
        planesCrashed = 0;
        
        landingQueue = new LinkedQueue<Plane> ();
        takeoffQueue = new LinkedQueue<Plane> ();
        
        crashHistory = new LinkedStack<Plane> ();
    }
    
    public void addPlane (Plane _p) {
        _p.setAddedTimestamp (currentTime);
        
        if (_p.getAction () == PlaneAction.LANDING)
            landingQueue.add (_p);
        else if (_p.getAction () == PlaneAction.TAKEOFF)
            takeoffQueue.add (_p);
    }
    
    public int getCurrentTime () {
        return currentTime;
    }
    
    public Plane getCurrentPlane () {
        return currentPlane;
    }
    
    public LinkedStack<Plane> getCrashedPlanes () {
        return crashHistory;
    }
    
    public void simulate () {
        // check if there is a plane landing or taking off
        if (currentPlane != null && currentPlane.getFinishedTimestamp () == -1) {
            // check if there is already a plane landing, and whether it finished
            if (currentPlane.getAction () == PlaneAction.LANDING &&
                (currentTime - currentPlane.getProcessedTimestamp () + 1) >= landingTime) {
                // record the time it finished landing
                currentPlane.setFinishedTimestamp (currentTime);
            }
            // check if there is already a plane taking off, and whether it finished
            else if (currentPlane.getAction () == PlaneAction.TAKEOFF &&
                (currentTime - currentPlane.getProcessedTimestamp () + 1) >= takeoffTime) {
                // record the time it finished taking off
                currentPlane.setFinishedTimestamp (currentTime);
            }
        }
        // check for more planes
        else {
            currentPlane = null;
            
            if (landingQueue.size () > 0) {
                Plane p = landingQueue.remove ();
                // take planes off the queue until there is a plane that has not crashed
                while (p != null && currentTime - p.getAddedTimestamp () > landingWaitLimit) {
                    // record the time that the plane crashed
                    p.setFinishedTimestamp (p.getAddedTimestamp () + landingWaitLimit);
                    crashHistory.push (p);
                    // get the next plane
                    if (!landingQueue.isEmpty () )
                        p = landingQueue.remove ();
                    else
                        p = null;
                }
                
                // start the landing process if there is a plane
                if (p != null) {
                    currentPlane = p;
                    currentPlane.setProcessedTimestamp (currentTime);
                }
            }
            else if (takeoffQueue.size () > 0) {
                // start the takeoff process
                currentPlane = takeoffQueue.remove ();
                currentPlane.setProcessedTimestamp (currentTime);
            }
        }
        
        // go to the next minute
        currentTime++;
    }
    
    private int currentTime;
    private Plane currentPlane;
    
    private double landingTime;
    private double takeoffTime;
    
    private int landingWaitLimit;
    
    private int planesLanded;
    private int planesTakenOff;
    private int planesCrashed;
    
    private LinkedQueue<Plane> landingQueue;
    private LinkedQueue<Plane> takeoffQueue;
    
    private LinkedStack<Plane> crashHistory;
}