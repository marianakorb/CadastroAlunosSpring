package com.senac.controller;
 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.senac.dao.AlunoJDBCdao;
import com.senac.model.Aluno;

import jakarta.servlet.http.HttpSession;
 
@Controller
public class Controlador {
	
	@Autowired
	AlunoJDBCdao dao;
	
	Aluno aluno = new Aluno();
		
	
	@GetMapping("/")
	public String paginaInicial() {
		return "index";
	}
	
	
	
	@GetMapping("Deslogar")
	public String deslogar() {
		HttpSession session = dao.getSession();
		session.invalidate();
		return "index";
	}
	

	
	@GetMapping("listarAlunos")
	public String listarAlunos (Model model) {		
		ArrayList<Aluno> listaAlunos= dao.listarAlunos();
		model.addAttribute("listaAlunos", listaAlunos);			
		return "listarAlunos";		
	}
	
	
	@GetMapping("cadastrarAluno")
	public String cadastrarAluno (Model model) {		
			
		return "cadastrarAluno";		
	}
	
	
	@PostMapping("autenticar")
	public String autenticar(
			@RequestParam("usuario") String usuario,
			@RequestParam("senha") String senha,
			Model model,
			HttpSession session) {
		
		
		if (usuario.equals("admin") && senha.equals("admin")) {
			
			session = dao.getSession();
			session.setMaxInactiveInterval(60);
			
			// Armazena o usuário na sessão
			session.setAttribute("usuario", usuario);
						
			return "redirect:/listarAlunos";			
			
		} else {
			model.addAttribute("error","1");
			return "index";
		}		
		
	}
	
		
		 @GetMapping("Alterar")
		 public String alterarAluno(
				 @RequestParam ("id") String id,
				 Model model
		    	 ) {
		         				
				// Recupera o atributo id do aluno que seve ser alterado
				aluno.setId(Integer.parseInt(id));
				
				aluno = dao.pesquisarPorId(aluno);		
 
				// Adiciona o aluno no request, para exibir seus dados na pagina de alterar
				//request.setAttribute("aluno", aluno);
				model.addAttribute("aluno", aluno);
				
				
				
				// Encaminhar a requisição para o JSP
				//RequestDispatcher dispatcher = request.getRequestDispatcher("alterarAluno.jsp");
				//dispatcher.forward(request, response);
			
			
		     return "alterarAluno";
	}
	
		
		
	@GetMapping("detalharAluno")
	public String detalharAluno(
			@RequestParam("id") String id,
			Model model,
			HttpSession session) {
		
		
		aluno.setId(Integer.parseInt(id));
		aluno = dao.pesquisarPorId(aluno);	
		
		//request.setAttribute("aluno", aluno);
		model.addAttribute("aluno", aluno);
		
 
		return "detalharAluno";
	}
	
	
	
	@GetMapping("excluirAluno")
	public String excluirAluno(
			@RequestParam("id") String id,
			Model model,
			HttpSession session) {
 
		aluno.setId(Integer.parseInt(id));
		dao.excluirAluno(aluno);
		
		
	
		
		return "redirect:/listarAlunos";	
	}

	
	
	@GetMapping("pesquisar")
	public String pesquisar(
			@RequestParam("valor") String valor,
			@RequestParam("tipoPesquisa") String tipoPesquisa,
			Model model,
			HttpSession session) {
		
					
		ArrayList<Aluno> listaAlunos= dao.pesquisar(valor,tipoPesquisa);
		
		System.out.println(listaAlunos);
		
		//request.setAttribute("listaAlunos", listaAlunos);
		model.addAttribute("listaAlunos", listaAlunos);
		
			
		return "listarAlunos";
	}

	
	
	@PostMapping("confirmarAlteracao")
	public String confirmarAlteracao(
			@RequestParam("id") String id,
			@RequestParam("matricula") String matricula,
			@RequestParam("nome") String nome,
			@RequestParam("idade") String idade,
			@RequestParam("genero") String genero,
			@RequestParam("semestre") String semestre,
			Model model,
			HttpSession session
		) {
	
 
		aluno.setId(Integer.parseInt(id));	
		aluno.setMatricula(matricula);	
		aluno.setNome(nome);
		aluno.setIdade(idade);
		aluno.setGenero(genero);
		aluno.setSemestre(semestre);	
		dao.alterarAluno(aluno);	
			
 
		model.addAttribute("aluno", aluno);
			
 
	
	return "detalharAluno";
 
}
	
	
 
	
	@PostMapping("confirmarCadastro")
	public String confirmarCadastro(
			@RequestParam("idade") String idade,
			@RequestParam("genero") String genero,
			@RequestParam("nome") String nome,
			@RequestParam("semestre") String semestre,			Model model,
			HttpSession session
			) {
		
		aluno.setNome(nome);
		aluno.setIdade(idade);
		aluno.setSemestre(semestre);
		aluno.setGenero(genero);
		String matricula = criarMatricula(idade,semestre);
		aluno.setMatricula(matricula);
		int id = dao.cadastrarAluno(aluno);	
		aluno.setId(id);
		
		model.addAttribute("aluno", aluno);
	
	
	return "redirect:/listarAlunos";
	}
	
	
private String criarMatricula(String idade, String semestre) {
		
		
		LocalDate dataAtual = LocalDate.now();
		int mes = dataAtual.getMonthValue();
		int ano = dataAtual.getYear();
		// Assume que o semestre 1 é de Janeiro a Junho e o semestre 2 é de Julho a Dezembro
		int semestreEscolha = (mes < 7) ? 1 : 2;
		
		Random random = new Random();		
		String matricula = String.valueOf(ano) + String.valueOf(mes) + String.valueOf(semestreEscolha) + String.valueOf(idade);
		
        // Gera quatro números aleatórios entre 0 e 9
        for (int i = 0; i < 4; i++) {
        	matricula += String.valueOf(random.nextInt(10)); 
        }
       
		return matricula;    
	}
	
	
}