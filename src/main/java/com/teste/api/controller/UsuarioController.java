package com.teste.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.internal.build.AllowSysOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teste.api.exception.InvalidCredentialsException;
import com.teste.api.model.dto.AuthenticationDTO;
import com.teste.api.model.dto.ReservasDTO;
import com.teste.api.model.entidades.Reservas;
import com.teste.api.model.entidades.Usuario;
import com.teste.api.service.TokenService;
import com.teste.api.service.UsuarioService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private HttpSession session;

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody @Valid AuthenticationDTO data) {

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				data.login(), data.senha());

		Authentication auth = this.authenticationManager.authenticate(usernamePasswordAuthenticationToken);

		var token = tokenService.gerarToken((Usuario) auth.getPrincipal());

		if (token == null) {
			throw new InvalidCredentialsException("Senha ou email invalidos");

		} else {
			List<ReservasDTO> reservas = usuarioService.obterReservaDoUsuario(data.login());
			session.setAttribute("carrinho", reservas);

			Map<String, String> response = new HashMap<>();
			response.put("token", token);

			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}

	@GetMapping("/carrinho")
	public ResponseEntity<?> getCarrinho(HttpSession session) {
		List<ReservasDTO> carrinhos = (List<ReservasDTO>) session.getAttribute("carrinho");

		if (carrinhos != null && !carrinhos.isEmpty()) {
			return ResponseEntity.ok(carrinhos);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body("Não há ingressos reservados");
		}
	}

	@PostMapping(path = "/criarUsuario")
	public ResponseEntity<Usuario> criaUsuario(@RequestBody @Valid Usuario data) {

		Usuario usuario = usuarioService.criaUsuario(data);

		if (usuario == null) {
			// return ResponseEntity.badRequest().body("e-mail ou cpf ja existe.");

		}

		return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
	}
//
//	@GetMapping("/buscaPorID/{id}")
//	public ResponseEntity<Usuario> getUsuarioPorId(@PathVariable int id)
//			throws ServiceNotInjectedException, RepositoryNotInjectedException {
//		Usuario usuarioId = usuarioService.obterUsuarioPorId(id);
//
//		if (usuarioId == null) {
//			throw new UsuarioNotFoundException("Usuario não existe!");
//		} else {
//			return ResponseEntity.ok(usuarioId);
//		}
//	}
//
//	@GetMapping
//	public ResponseEntity<List<Usuario>> getListUsuario() throws RepositoryNotInjectedException {
//		List<Usuario> usuarios = usuarioService.listClient();
//		return new ResponseEntity<List<Usuario>>(usuarios, HttpStatus.OK);
//
//	}
//
//	@PutMapping
//	public ResponseEntity<Usuario> getAtualizaUsuario(@RequestBody Usuario usuario)
//			throws RepositoryNotInjectedException {
//		usuarioService.atualizaUsuario(usuario);
//		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
//
//	}

}
