public class Main {
    public static void main(String[] args) {
        Ranking r = new Ranking();
        r.registrar("Ashe", 1800);
        r.registrar("Mein", 1900);
        r.registrar("Karlota", 1000);
        r.registrar("Renesil", 900);
        r.registrar("Isaelop", 1200);
        r.registrar("Vann", 700);

        r.mostrarTop(5);
        System.out.println("Ganador: " + r.campeon());

        System.out.println("\nRenesil hace una partida increíble...\n");
        r.registrar("Renesil", 2000);

        r.mostrarTop(5);
        System.out.println("Ganador: " + r.campeon());

        System.out.println("\nPosición de Vann: " + r.posicionDe("Vann"));
    }
}
