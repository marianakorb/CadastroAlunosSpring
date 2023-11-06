<%@page import="java.util.ArrayList, java.util.List, com.senac.model.Aluno" %>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
 
<%
	String usuario = (String) session.getAttribute("usuario");
	if (usuario == null) {
		response.sendRedirect("index?error=2");
	}
%>
 
	<h2>Dados do aluno</h2>	
  <%  Aluno aluno = (Aluno) request.getAttribute("aluno"); %>
 
	
	<%= aluno.getId() %>
	
	
	<br><br>
	Matricula: <%= aluno.getMatricula()%>
	<br><br>
	
	Nome: <%= aluno.getNome() %>
	<br><br>
	
	Idade: <%= aluno.getIdade() %>
	<br><br>
	
	Genero: <%= aluno.getGenero() %>
	<br><br>
	
	
	Semestre: <%= aluno.getSemestre() %>
	<br><br>
	
	
		
	<input type="button"  onclick="javascript:location.href='listarAlunos'"  value="Voltar">	
	<a href="Alterar?id=<%=aluno.getId()%>">Alterar</a>
	<br><br>
	
	
</body>
</html>