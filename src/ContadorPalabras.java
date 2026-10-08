import java.util.Map;
import java.util.TreeMap;

public class ContadorPalabras {
    public static void main(String[] args) {
        String texto = "java es java y map es util";
        String[] palabras = texto.split(" ");

        // TreeMap: las claves quedan ordenadas alfabéticamente
        Map<String, Integer> frecuencias = new TreeMap<>();

        for (String palabra : palabras) {
            // Si la palabra ya existe suma 1; si no, la inserta con valor 1
            frecuencias.merge(palabra, 1, Integer::sum);
        }

        for (Map.Entry<String, Integer> par : frecuencias.entrySet()) {
            System.out.println(par.getKey() + ": " + par.getValue());
        }
    }
}