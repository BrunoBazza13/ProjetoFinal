package com.teste.api.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.teste.api.model.entidades.Ingresso;
import com.teste.api.model.entidades.Reservas;
import com.teste.api.model.entidades.Usuario;

public interface ReservaRepository extends JpaRepository<Reservas, Integer> {

	@Query("SELECT COALESCE(SUM(ic.quantidadeIngresso), 0) FROM Reservas ic " + "JOIN ic.ingresso i "
			+ "JOIN i.setor s " + "WHERE s.id = :setorId")
	int getTotalQuantidadePorSetor(@Param("setorId") int setorId);

	Reservas findByUsuarioAndIngresso_Id(Usuario usuario, int ingressoId);

	 Optional<Reservas> findByIngresso(Ingresso ingresso);

}
