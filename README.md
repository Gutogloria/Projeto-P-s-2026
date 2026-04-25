# 📧 Projeto Pós 2026 - Comunicação via SMTP/IMAP

Este projeto foca-se na integração de envio e receção de e-mails diretamente em aplicações Android utilizando **Kotlin** e a **JavaMail API**. O objetivo é permitir que o app interaja com servidores de e-mail de forma nativa e segura.

---

## 👥 Equipe do Projeto
| Membro | GitHub/Contato |
| :--- | :--- |
| **Breno Zaffalon** | @BrenoZaffalon |
| **Daniel Kaghofer** | @DanielKaghofer |
| **Jaqueline** | @Jaqueline |
| **Willyan Patrykc** | @WillyanPatrykc |
| **Thiago Monteiro** | @ThiagoMonteiro |
| **Gustavo Da Glória** | @GustavoDaGloria |

* **Horário de Reunião/Apresentação:** 08:50h

---

## 🚀 Como Colaborar
1. **Ideias:** Edite este arquivo para adicionar sugestões na seção de tarefas.
2. **Desenvolvimento:** Suba os arquivos Kotlin para a pasta `/app`.
3. **Padrão:** Documente todas as alterações e utilize comentários no código.

---

## 📚 Documentação Técnica

### 1. Protocolos de Comunicação
Para uma integração eficiente, o projeto baseia-se em dois protocolos fundamentais:

* **SMTP (Simple Mail Transfer Protocol):** Protocolo padrão para o **envio** de mensagens. 
    * *Portas Comuns:* 465 (SSL) ou 587 (TLS). 
* **IMAP (Internet Message Access Protocol):** Utilizado para **acessar e gerenciar** e-mails no servidor, garantindo a sincronização entre dispositivos. 



### 2. Fluxo de Implementação (JavaMail API)
A integração segue o fluxo lógico estruturado abaixo: 

1.  **Configurar Propriedades:** Definição do servidor (ex: `smtp.gmail.com`). 
2.  **Autenticação:** Criação do objeto `Authenticator` com as credenciais. 
3.  **Sessão:** Estabelecimento da `Session` com o servidor de e-mail. 
4.  **Mensagem:** Construção do conteúdo através da classe `MimeMessage`. 
5.  **Transporte:** Disparo da mensagem via `Transport.send()`. 

---

## 📋 Lista de Tarefas (Backlog)
- [ ] Configurar dependências do Gradle para JavaMail/Jakarta Mail.
- [ ] Implementar classe de autenticação segura.
- [ ] Criar interface UI simples para envio de teste.
- [ ] Validar suporte a anexos.

---
*Última atualização: Abril de 2026*
