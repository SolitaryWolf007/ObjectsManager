package com.ericsonradaelli.objectsmanager.menus;

import com.ericsonradaelli.objectsmanager.entities.Emprestimo;
import com.ericsonradaelli.objectsmanager.entities.Objeto;
import com.ericsonradaelli.objectsmanager.entities.Pessoa;
import com.ericsonradaelli.objectsmanager.entities.TipoObjeto;
import com.ericsonradaelli.objectsmanager.helpers.Entrada;
import com.ericsonradaelli.objectsmanager.services.EmprestimoService;
import com.ericsonradaelli.objectsmanager.services.ManutencaoService;
import com.ericsonradaelli.objectsmanager.services.ObjetoService;
import com.ericsonradaelli.objectsmanager.services.PessoaService;
import com.ericsonradaelli.objectsmanager.services.TipoObjetoService;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author erics
 */
public class MenuListagem {
    
    public static void EmprestimosPorData(){
        String inputData = Entrada.leiaString("========================[ Object Manager | Listagem Avançada ]========================\n> Indique a Data para pesquisar\n> Formato DD/MM/YYYY.");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            if(dateFormat.parse(inputData) != null){
                
                ArrayList<Emprestimo> emprestimos = EmprestimoService.getByDate(inputData);
                String listaTipos = "========================[ Object Manager | Listagem Avançada ]========================\n> Empréstimos com a Data: "+inputData+
                "\nID | PESSOA | OBJETO | ESTADO | RETIRADA | DEVOLUÇÃO\n";

                for (Emprestimo emp : emprestimos) {

                    String nomePessoa = "("+String.valueOf(emp.getPessoa())+")";
                    Pessoa checkPessoa = PessoaService.get(emp.getPessoa());
                    if(checkPessoa != null){ nomePessoa = nomePessoa+" "+checkPessoa.getNome(); }

                    String nomeObj = "("+String.valueOf(emp.getObjeto())+")";
                    Objeto checkObjeto = ObjetoService.get(emp.getObjeto());
                    if(checkObjeto != null){ nomeObj = nomeObj+" "+checkObjeto.getNome(); }

                    String empEstado = "Devolvido";
                    if( emp.isAtivo() ){ empEstado = "Retirado"; }

                    listaTipos += ( "#"+emp.getId()+" | "+nomePessoa+" | "+nomeObj+" | "+empEstado+" | "+emp.getDataRetirada()+" | "+emp.getDataDevolvido()+"\r\n" );
                }
                Entrada.leiaBoolean(listaTipos,"OK","Fechar"); 
            }                  
        } catch (Exception e) {
            Entrada.leiaBoolean("Data inserida não está em um formato válido!","OK","Fechar");
        }
    }
    
    public static void ObjetosPorSituacao(){
        
        boolean exitLoop = false;
        while (!exitLoop){        
        
            String listaTipos = "";
            ArrayList<TipoObjeto> tipos = TipoObjetoService.getAll();
            for (TipoObjeto tipo : tipos) { listaTipos += ( "#"+tipo.getId()+" | "+tipo.getDescricao()+"\r\n" ); }
            // LER TIPO
            int nTipo = Entrada.leiaInt("========================[ Object Manager | Listagem Avançada ]========================\n[ Pesquisar ]\n> Gostaria de filtrar por Tipo?\n\n0) Ignorar\n===[ TIPOS ]===\n"+listaTipos,"0");
            TipoObjeto tipoObj = TipoObjetoService.get(nTipo);
            while (tipoObj == null){
                if(nTipo == 0) break;
                nTipo = Entrada.leiaInt("========================[ Object Manager | Listagem Avançada ]========================\n[ Pesquisar ]\n> Tipo informado é inválido!!\n> Gostaria de filtrar por Tipo?\n\n0) Ignorar\n===[ TIPOS ]===\n"+listaTipos);
                tipoObj = TipoObjetoService.get(nTipo);
            }
              
            // ESPECIFICAR CARACTERISTICA
            String loopText = 
                    "=========================[ Object Manager | Listagem Avançada ]=========================\n"+
                    "[ Pesquisar ]\n"+
                    "> Tipo Indicado: #"+nTipo+"\n"+
                    "> Indique a característica:\n"+
                    "1) Objetos Emprestados\n"+
                    "2) Objetos Disponíveis\n"+
                    "3) Objetos Baixados\n"+
                    "4) Objetos em Manutenção"+
                    "\n0) Cancelar";

                int option = Entrada.leiaInt(loopText);
                
                ArrayList<Objeto> objetos = ObjetoService.getObjectsWithFilter(nTipo);
                 
                switch (option) {
                    case 0:
                        exitLoop = true;
                        break;
                    case 1:
                        String objetosEmprestados = "--------[ EMPRESTADOS ]--------\nID | NOME | DESCRIÇÃO | TIPO | ESTADO\n";
                        String objetosNaoEmprestados = "\n\n--------[ LIVRES ]--------\nID | NOME | DESCRIÇÃO | TIPO | ESTADO\n";
                        for (Objeto objeto : objetos) {
                            String estado = "Ativo";
                            if (!objeto.isAtivo()){ estado = "Baixado"; }

                            String descTipo = "("+String.valueOf(objeto.getTipo())+")";
                            TipoObjeto checkTipo = TipoObjetoService.get(objeto.getTipo());
                            if(checkTipo != null){ descTipo += " "+checkTipo.getDescricao(); }

                            if( EmprestimoService.isAvaliable(objeto.getId()) ){
                                objetosNaoEmprestados += ( "#"+objeto.getId()+" | "+objeto.getNome()+" | "+objeto.getDescricao()+" | "+descTipo+" | "+estado+"\r\n" );
                            }else{
                                objetosEmprestados += ( "#"+objeto.getId()+" | "+objeto.getNome()+" | "+objeto.getDescricao()+" | "+descTipo+" | "+estado+"\r\n" );
                            }
                            
                        } 
                        Entrada.leiaBoolean("=========================[ Object Manager | Listagem Avançada ]=========================\n"+objetosEmprestados+objetosNaoEmprestados,"OK","Fechar");     
                        exitLoop = true;
                        break;
                          
                    case 2:
                        String objetosDisponiveis = "--------[ DISPONÍVEIS ]--------\nID | NOME | DESCRIÇÃO | TIPO | ESTADO\n";
                        String objetosNaoDisponiveis = "\n\n--------[ OCUPADOS ]--------\nID | NOME | DESCRIÇÃO | TIPO | ESTADO\n";
                        for (Objeto objeto : objetos) {
                            
                            String estado = "Ativo";
                            if (!objeto.isAtivo()){ estado = "Baixado"; }

                            String descTipo = "("+String.valueOf(objeto.getTipo())+")";
                            TipoObjeto checkTipo = TipoObjetoService.get(objeto.getTipo());
                            if(checkTipo != null){ descTipo += " "+checkTipo.getDescricao(); }

                            if ( objeto.isAtivo() && !ManutencaoService.inService(objeto.getId()) && EmprestimoService.isAvaliable(objeto.getId()) ){
                                objetosDisponiveis += ( "#"+objeto.getId()+" | "+objeto.getNome()+" | "+objeto.getDescricao()+" | "+descTipo+" | "+estado+"\r\n" );
                            }else{
                                objetosNaoDisponiveis += ( "#"+objeto.getId()+" | "+objeto.getNome()+" | "+objeto.getDescricao()+" | "+descTipo+" | "+estado+"\r\n" );
                            }
                            
                        } 
                        Entrada.leiaBoolean("=========================[ Object Manager | Listagem Avançada ]=========================\n"+objetosDisponiveis+objetosNaoDisponiveis,"OK","Fechar");     
                        exitLoop = true;
                        break;
                    case 3:
                        String objetosAtivos = "--------[ ATIVOS ]--------\nID | NOME | DESCRIÇÃO | TIPO | ESTADO\n";
                        String objetosNaoAtivos = "\n\n--------[ BAIXADOS ]--------\nID | NOME | DESCRIÇÃO | TIPO | ESTADO\n";
                        for (Objeto objeto : objetos) {
                            
                            String estado = "Ativo";
                            if (!objeto.isAtivo()){ estado = "Baixado"; }

                            String descTipo = "("+String.valueOf(objeto.getTipo())+")";
                            TipoObjeto checkTipo = TipoObjetoService.get(objeto.getTipo());
                            if(checkTipo != null){ descTipo += " "+checkTipo.getDescricao(); }

                            if ( objeto.isAtivo() ){
                                objetosAtivos += ( "#"+objeto.getId()+" | "+objeto.getNome()+" | "+objeto.getDescricao()+" | "+descTipo+" | "+estado+"\r\n" );
                            }else{
                                objetosNaoAtivos += ( "#"+objeto.getId()+" | "+objeto.getNome()+" | "+objeto.getDescricao()+" | "+descTipo+" | "+estado+"\r\n" );
                            }
                            
                        } 
                        Entrada.leiaBoolean("=========================[ Object Manager | Listagem Avançada ]=========================\n"+objetosAtivos+objetosNaoAtivos,"OK","Fechar");     
                        exitLoop = true;
                        break;
                    case 4:
                        String objetosManutencao = "--------[ EM MANUTENÇÃO ]--------\nID | NOME | DESCRIÇÃO | TIPO | ESTADO\n";
                        String objetosForaManutencao = "\n\n--------[ EM FUNCIONAMENTO ]--------\nID | NOME | DESCRIÇÃO | TIPO | ESTADO\n";
                        for (Objeto objeto : objetos) {
                            
                            String estado = "Ativo";
                            if (!objeto.isAtivo()){ estado = "Baixado"; }

                            String descTipo = "("+String.valueOf(objeto.getTipo())+")";
                            TipoObjeto checkTipo = TipoObjetoService.get(objeto.getTipo());
                            if(checkTipo != null){ descTipo += " "+checkTipo.getDescricao(); }

                            if ( ManutencaoService.inService(objeto.getId()) ){
                                objetosManutencao += ( "#"+objeto.getId()+" | "+objeto.getNome()+" | "+objeto.getDescricao()+" | "+descTipo+" | "+estado+"\r\n" );
                            }else{
                                objetosForaManutencao += ( "#"+objeto.getId()+" | "+objeto.getNome()+" | "+objeto.getDescricao()+" | "+descTipo+" | "+estado+"\r\n" );
                            }
                            
                        } 
                        Entrada.leiaBoolean("=========================[ Object Manager | Listagem Avançada ]=========================\n"+objetosManutencao+objetosForaManutencao,"OK","Fechar");     
                        exitLoop = true;
                        break;
                }
        }
    }
}