package com.aluracursos.sceenmatch;

import com.aluracursos.sceenmatch.principal.Principal;
import com.aluracursos.sceenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SceenmatchApplication implements CommandLineRunner {

	@Autowired
	private SerieRepository repository;
	public static void main(String[] args) {
		SpringApplication.run(SceenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal;
        principal = new Principal(repository);
        principal.muestraMenú();
		}
	}

