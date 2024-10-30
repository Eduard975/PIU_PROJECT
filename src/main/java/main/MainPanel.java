package main;

public class MainPanel implements Runnable {
    final int UPS = 60;
    int FPS = 60;
    boolean running = true;
    boolean RENDER_TIME = true;
    @Override
    public void run() {
        long initialTime = System.nanoTime();
        final double timeU = (double) 1000000000 / UPS;
        final double timeF = (double) 1000000000 / FPS;
        double deltaU = 0, deltaF = 0;
        int frames = 0, ticks = 0;
        long timer = System.currentTimeMillis();

        while (running) {

            long currentTime = System.nanoTime();
            deltaU += (currentTime - initialTime) / timeU;
            deltaF += (currentTime - initialTime) / timeF;
            initialTime = currentTime;

            if (deltaU >= 1) {
                //getInput();
                //update();
                ticks++;
                deltaU--;
            }

            if (deltaF >= 1) {
                //render();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - timer > 1000) {
                if (RENDER_TIME) {
                    System.out.println(String.format("UPS: %s, FPS: %s", ticks, frames));
                }
                frames = 0;
                ticks = 0;
                timer += 1000;
            }
        }
    }
}
