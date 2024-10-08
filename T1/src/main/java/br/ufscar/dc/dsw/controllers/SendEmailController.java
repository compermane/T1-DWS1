package br.ufscar.dc.dsw.controllers;

import java.io.IOException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.util.List;

import br.ufscar.dc.dsw.domain.Locacao;
import br.ufscar.dc.dsw.domain.Usuario;
import br.ufscar.dc.dsw.dao.LocacaoDAO;
import br.ufscar.dc.dsw.domain.Locadora;

@WebServlet(urlPatterns = { "/SendEmail" })
public class SendEmailController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[+] Método get de SendEmailControllerExecutado");

        // Configurações do servidor de email
        String host = "smtp.gmail.com";
        String porta = "587";
        final String username = "deessedabliuteste@gmail.com";
        final String senha = "txhx bfkr gbwk eucw";

        // Propriedades do servidor de email
            Properties propriedades = new Properties();
            propriedades.put("mail.smtp.host", host);
            propriedades.put("mail.smtp.port", porta);
            propriedades.put("mail.smtp.auth", "true");
            propriedades.put("mail.smtp.starttls.enable", "true");

        // Autenticação do usuário
        Authenticator autenticador = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, senha);
            }
        };

        // Criação de uma nova sessão de email
        Session sessao = Session.getInstance(propriedades, autenticador);
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogado");
        Locadora locadora = (Locadora) request.getAttribute("locadoraParaEmail");

        System.out.println("BRUHHHHHHHHHHHHHHHHHHHH" + locadora.getEmail());

        String toEmails = usuario.getEmail() + ", " + locadora.getEmail();
        
        final Message mensagem = new MimeMessage(sessao);
        try {
            mensagem.setFrom(new InternetAddress(username));
            mensagem.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmails));
            mensagem.setSubject("Locação de Bicicleta Realizada");
            mensagem.setText("Nome do cliente: " + usuario.getNome() + "\nEmail do cliente: " + usuario.getEmail()
                    + "\n\nNome da locadora: " + locadora.getNome() + "\nEmail da locadora: " + locadora.getEmail()
                    + "\n\nData da locação: " + request.getAttribute("dataLocacao") + "\nHorário da locação: " + request.getAttribute("horarioLocacao") + "\n\n");
            Transport.send(mensagem);
        } 
        catch (MessagingException e) {
            e.printStackTrace();
        }
        
        System.out.println("[+] Email enviado para " + toEmails);

        LocacaoDAO locacaoDAO = new LocacaoDAO();
        List<Locacao> listaLocacoes = locacaoDAO.getAll(usuario.getId());
        request.getSession().setAttribute("listaLocacoes", listaLocacoes);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/home/cliente/tela_inicial.jsp");
        dispatcher.forward(request, response);
    }
}