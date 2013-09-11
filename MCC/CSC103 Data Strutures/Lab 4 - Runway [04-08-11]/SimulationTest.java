public class SimulationTest {
    public static void main (String[] args) {
        Runway r = new Runway (5, 3, 8);
        
        Plane p1 = new Plane (0, PlaneAction.LANDING);
        Plane p2 = new Plane (0, PlaneAction.LANDING);
        Plane p3 = new Plane (0, PlaneAction.LANDING);
        Plane p4 = new Plane (0, PlaneAction.TAKEOFF);
        
        r.addPlane (p4);
        r.addPlane (p1);
        r.addPlane (p2);
        r.addPlane (p3);
        
        for (int i = 0; i < 20; i++) {
            r.simulate ();
            
            System.out.println ("Current minute: " + r.getCurrentTime () );
            
            Plane p = r.getCurrentPlane ();
            if (p != null) {
                if (p.getAction () == PlaneAction.LANDING) {
                    if (p.getFinishedTimestamp () == -1)
                        System.out.println ("Plane #" + p.getNumber () + " is currently landing.");
                    else
                        System.out.println ("Plane #" + p.getNumber () + " has landed.");
                }
                else if (p.getAction () == PlaneAction.TAKEOFF) {
                    if (p.getFinishedTimestamp () == -1)
                        System.out.println ("Plane #" + p.getNumber () + " is currently taking off.");
                    else
                        System.out.println ("Plane #" + p.getNumber () + " has taken off.");
                }
            }
        }
        
        LinkedStack<Plane> crashedPlanes = r.getCrashedPlanes ();
        while (!crashedPlanes.isEmpty () ) {
            Plane p = crashedPlanes.pop ();
            
            System.out.println ("Plane #" + p.getNumber () + " crashed at minute " + p.getFinishedTimestamp () );
        }
    }
}