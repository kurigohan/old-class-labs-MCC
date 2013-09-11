public class Plane {
    public Plane (PlaneAction _action) {
        addedTimestamp = _timestamp;
        number = currentNumber;
        action = _action;
        
        processedTimestamp = -1;
        finishedTimestamp = -1;
        currentNumber++;
    }
    
    public int getNumber () {
        return number;
    }
    
    public int getAddedTimestamp () {
        return addedTimestamp;
    }
    
    public int getProcessedTimestamp () {
        return processedTimestamp;
    }
    
    public int getFinishedTimestamp () {
        return finishedTimestamp;
    }
    
    public PlaneAction getAction () {
        return action;
    }
    
    public void setAddedTimestamp (int _timestamp) {
        processedTimestamp = _timestamp;
    }
    
    public void setProcessedTimestamp (int _timestamp) {
        processedTimestamp = _timestamp;
    }
    
    public void setFinishedTimestamp (int _timestamp) {
        finishedTimestamp = _timestamp;
    }
    
    private int addedTimestamp;
    private int processedTimestamp;
    private int finishedTimestamp;
    private int number;
    private PlaneAction action;
    
    private static int currentNumber = 1;
}