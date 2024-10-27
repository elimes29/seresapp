package com.aluracursos.sceenmatch.principal;

import com.aluracursos.sceenmatch.model.*;
import com.aluracursos.sceenmatch.repository.SerieRepository;
import com.aluracursos.sceenmatch.service.ConsumoApi;
import com.aluracursos.sceenmatch.service.ConvierteDatos;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=" + getApiKeyOmdb();
    private ConvierteDatos conversor = new ConvierteDatos();
    List<DatosSerie> datosSeries = new ArrayList<>();
    private SerieRepository  repositorio;
    private List<Serie> series;
    private Optional<Serie> serieBuscada;

    //Buscando el valor de la api key de ODBM que es una variable del sistema
    private static String getApiKeyOmdb() {
        String apiKeyOmdb = System.getenv("API_KEY_OMDB");
        if (apiKeyOmdb == null) {
            throw new IllegalStateException("La variable de entorno API_KEY_OMDB no está definida.");
        }
        return apiKeyOmdb;
    }

    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }

    public void muestraMenú(){

        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar serie por título
                    5 - Top 5 mejores series
                    6 - Buscar series por categorias         
                    7 - Buscar serie que tengan menos de X temporadas y una evaluación Y    
                    8 - Buscar episodios por nombre
                    9 - Top 5 episodios por serie
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCateoria();
                    break;
                case 7:
                    buscarSeriePorNumeroDeTemporadasYEvaluacion();
                    break;
                case 8:
                    buscarEpisodisoPorNombre();
                    break;
                case 9:
                    buscarTop5Episodios();
                    break;

                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }
    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie de la que quieres ver el episodio");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toUpperCase().contains(nombreSerie.toUpperCase()))
                .findFirst();
        if (serie.isPresent()){
            Serie serieEncontrada = serie.get();
            List<DatosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalDeTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporada datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(t -> t.episodios().stream()
                            .map(e -> new Episodio(t.numero(),e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }else {
            System.out.println("No se encotró serie");
        }


    }
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        //datosSeries.add(datos);
        Serie serie = new Serie(datos);
        repositorio.save(serie);
        System.out.println(datos);
    }
    private void mostrarSeriesBuscadas() {
        series = repositorio.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);

    }

    public void buscarSeriePorTitulo(){

        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        serieBuscada = repositorio.findByTituloContainsIgnoreCase(nombreSerie);

        if (serieBuscada.isPresent()){
            System.out.println("La serie buscada es: " + serieBuscada.get());
        }else {
            System.out.println("Serie no encontrada");
        }
    }

    private void buscarTop5Series() {
        List<Serie> series5 = repositorio.findTop5ByOrderByEvaluacionDesc();
        series5.stream()
                .map(s -> "Titulo: " + s.getTitulo() + " / Puntuación: " +s.getEvaluacion())
                .forEach(System.out::println);
    }

    private void buscarSeriesPorCateoria(){
        System.out.println("Escriba el genero/categiria de las series que desea buscar");
        var genero = teclado.nextLine();
        Categoria categoria = Categoria.fromEspanol(genero);
        List<Serie> seriesEncontradas = repositorio.findByGenero(categoria);

        if(seriesEncontradas.size()>0){
            seriesEncontradas.stream()
                    .map(s -> "Titulo: " + s.getTitulo() + " / Categoría: " + s.getGenero())
                    .forEach(System.out::println);
        }else {
            System.out.println("No hay series con ese género/categoría");
        }
    }

    private void buscarSeriePorNumeroDeTemporadasYEvaluacion(){
        System.out.println("Ingrese el número maximo de tempradas");
        int numeroDeTemporadas = teclado.nextInt();
        teclado.hasNextLine();
        System.out.println("Ingrese la evaluación");
        Double evaluacion = teclado.nextDouble();

        //List<Serie> seriesEncontradas = repositorio
          //      .findByEvaluacionGreaterThanAndTotalDeTemporadasLessThan(evaliacion, numeroDeTemporadas+1);

        List<Serie> seriesEncontradas = repositorio.seriesPorTemporadaYEvaluacion(evaluacion, numeroDeTemporadas+1);

        if(seriesEncontradas.size()>0){
            seriesEncontradas.stream()
                    .map(s -> "Titulo: " + s.getTitulo() + " / # de temporadas: " + s.getTotalDeTemporadas()
                            + " / evaluación: " + s.getEvaluacion())
                    .forEach(System.out::println);
        }else {
            System.out.println("No hay series con esas características");
        }



    }

    private void buscarEpisodisoPorNombre(){
        System.out.println("Escribe el nombre del episodio");
        String nombreEpisodio = teclado.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e -> System.out.printf("Serie: %s Temporada: %s Episodio: %s Evaluación %s\n"
                , e.getSerie().getTitulo(), e.getNumeroTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()) );
    }


    private void buscarTop5Episodios(){
        buscarSeriePorTitulo();
        System.out.println("****************************");
        if (serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.top5Episodios(serie);
            topEpisodios.forEach(e -> System.out.printf("Episodio: %s Temporada: %s Episodio: %s Evaluación %s\n"
                    , e.getTitulo(), e.getNumeroTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()) );
        }
    }
}



