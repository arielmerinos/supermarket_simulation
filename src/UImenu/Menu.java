package UImenu;

import market.SuperMarket;
import market.admin.Product;
import serializer.Serializer;
import sim.Simulation;
import util.Lista;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import static util.Utilities.getFloat;
import static util.Utilities.getInt;

/**
 * <h1>Menu</h1>
 *
 * @author Ariel Merino & Armando Aquino
 * @version 1.0
 */
public class Menu {

    /**
     *
     */

    /**
     *
     */
    private Serializer serializer;

    /**
     *
     */
    private Simulation simulation;

    public Menu(){
        serializer = new Serializer();
    }

    /**
     *
     */
    public void mainMenu(){
        System.out.println("Bienvedidx al supermercado");
        checkExistences();
        boolean continuar = true;
        do {
            System.out.println("\n[1]Modo Administrativo");
            System.out.println("[2]Modo Usuario");
            System.out.println("[3]Salir\n");
            int opcion = getInt("Ingrese la opción deseada: ", "Error, intente de nuevo");
            switch (opcion){
                case 1:
                    adminMenu();
                    break;
                case 2:
                    userMenu();
                    break;
                case 3:
                    System.out.println("¡Hasta pronto!");
                    continuar = false;
                default:
                    System.out.println("Ingrese una opción válida");
                    break;

            }

        }while (continuar);
    }

    /**
     *
     */
    private void save(){
        serializer.write(simulation, "dataD.ser");
    }

    /**
     *
     */
    private void checkExistences(){
        File file = new File("dataD.ser");
        if (file.exists()){
            simulation = (Simulation) serializer.read("dataD.ser");
        }else {
            save();
        }
    }

    /**
     *
     */
    private void adminMenu(){
        boolean continua = true;
        do {
            System.out.println("\n[1]Dar de Alta algún prodcuto");
            System.out.println("[2]Resurtir existencias de algún producto");
            System.out.println("[3]Generar productos aleatorios y cargarlo al inventario");
            System.out.println("[4]Ver el inventario");
            System.out.println("[5]Regresar al menú principal\n");

            int anInt = getInt("Ingrese la opción deseada: ", "Error, intente de nuevo");

            switch (anInt){
                case 1:
                    simulation.getCostco().darAltaProducto(addProduct());
                    save();
                    break;
                case 2:
                    if (resurtirProducto()){
                        System.out.println("Se surtío correctamente");
                    }else {
                        System.out.println("intente de nuevo, ocurrió un error");
                    }
                    save();
                    break;
                case 3:
                    int numToGenerate = getInt("Ingrese el número de productos que desea generar(recomendable >100): ",
                            "Error, intente de nuevo con un número válido");
                    break;
                case 4:
                    System.out.println(simulation.getCostco().getAlmacen().getWhareHouse());
                    break;
                case 5:
                    continua = false;
                    break;
                default:
                    System.out.println("Ingrese una opción válida");
                    break;
            }
        }while (continua);
    }

    /**
     *
     */
    private void userMenu(){
        boolean conti = true;
        do {
            System.out.println("[1]Elaborar una simulación automática");
            System.out.println("[2]Elegir cuántas cajas se quieren simular");
            System.out.println("[3]Regresar el menú principal");
            int answer = getInt("Ingrese la opción deseada: ", "Error, intente de nuevo");
            switch (answer){
                case 1:
                    System.out.println("Simulando compras... espere por favor");
                    Lista<String> plotting = new Lista<>();
                    for (int i = 1; i < 15; i++) {

                        simulation = new Simulation(i,15-i,2);
                        simulation.simulate();
                        simulation.getReports();
                        plotting.agregar(simulation.writeLine());
                    }
                    serializer.writeTXT(plotting, "datos.dat");
                    System.out.println("Terminó con éxito la simulación");
                    break;
                case 2:
                    execPlot();
                    break;
                case 3:
                conti = false;
                default:
                    System.out.println("Ingrese una opción válida");
                    break;
            }
        }while (conti);
        System.out.println();
    }

    /**
     *
     * @return
     */
    private Product addProduct(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el nombre del producto: ");
        String name = scanner.nextLine();
        int units = getInt("Ingrese el número de existencias del producto: ", "Error, intente de nuevo");
        float price = getFloat("Ingrese el precio del producto: ", "Error, intente de nuevo");
        return new Product(units,name,price);
    }

    /**
     *
     * @return
     */
    private boolean resurtirProducto(){
        int numero = getInt("Ingrese el id del producto a resurtir: ", "Error, intente de nuevo");
        Product inCuestion = new Product(numero);
        if (simulation.getCostco().getAlmacen().getWhareHouse().contiene(inCuestion)){
            int nuevo = getInt("Ingrese el número de elementos a agregar: ", "Error, intente de nuevo");
            return simulation.getCostco().getAlmacen().modifyStock(nuevo, numero);
        }
        return false;
    }

    private void execPlot(){
        boolean contAsking = true;
        do {
            System.out.println("¿Desea generar las gráficas de los resultados?\n[1]Sí\n[2]No");
            int ans = getInt("Ingrese la opción deseada", "Error, intente de nuevo");
            switch (ans){
                case 1:
                    boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
                    if (!isWindows){
                        try {
                            Process processBuilder = Runtime.getRuntime().exec("./histGraph.sh");
                            Process openImg = Runtime.getRuntime().exec("mimeopen plot/histGraph.png");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        System.out.println("Lo sentimos mucho, para windows no funcionamos así ;( intente en linux :)");
                    }
                    contAsking = false;
                    break;
                case 2:
                    System.out.println("Ok!");
                    contAsking = false;
                    break;
                default:
                    System.out.println("Ingrese una de las opciones válidas");
                    break;
            }
        }while (contAsking);
    }
}
