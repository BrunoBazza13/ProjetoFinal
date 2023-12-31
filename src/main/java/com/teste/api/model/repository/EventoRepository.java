package com.teste.api.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.teste.api.model.entidades.Evento;

public interface EventoRepository extends JpaRepository<Evento, Integer>{

	
	@Query("SELECT e.nomeEvento, e.descricao, e.data, e.atracao FROM Evento e")
	 List<Evento> retornaDadosEvento();
	
}
