package com.ericsonradaelli.objectsmanager.menus;

import com.ericsonradaelli.objectsmanager.entities.Manutencao;
import com.ericsonradaelli.objectsmanager.entities.Objeto;
import com.ericsonradaelli.objectsmanager.helpers.Entrada;
import com.ericsonradaelli.objectsmanager.services.EmprestimoService;
import com.ericsonradaelli.objectsmanager.services.ManutencaoService;
import com.ericsonradaelli.objectsmanager.services.ObjetoService;
import java.util.ArrayList;

/**
 *
 * @author erics
 */
public class MenuManutencoes {
    //Completo - Testado
    public static void show() {

        boolean exitMenu = false;

        while (!exitMenu){        
            String menuText = 
                "===========================[ Object Manager | Manutenções ]===========================\n"+
                "1) Incluir Manutenção\n"+
                "2) Consultar Manutenção\n"+
                "3) Alterar Manutenção\n"+
                "4) Excluir Manutenção\n"+
                "5) Listar Manutenções\n"+
                "\n0) Menu Principal";
            
            int option = Entrada.leiaInt(menuText);           

            switch (option) {
                case 0:
                    exitMenu = true;
                    break;
                case 1:
                    CadastrarManutencao();
                    break;
                case 2:
                    ConsultarManutencao(true);
                    break;
                case 3:
                    AlterarManutencao();
                    break;
                case 4:
                    ExcluirManutencao();
                    break;
                case 5:
                    ListarManutencoes();
                    break;
            }
            
        }
    }
    
    private static void CadastrarManutencao() {
        
        // LER OBJETOS
        String listaObjetos = "";
        ArrayList<Objeto> objetos = ObjetoService.getAll();
        for (Objeto listObjeto : objetos) {
            int id = listObjeto.getId();
            if ( listObjeto.isAtivo() && !ManutencaoService.inService(id) && EmprestimoService.isAvaliable(id) ){
                listaObjetos += ( "#"+id+" | "+listObjeto.getNome()+" | "+listObjeto.getDescricao()+"\r\n" );
            }
        }
        int mObjeto = Entrada.leiaInt("===========================[ Object Manager | Manutenções ]===========================\nCadastro (1/3)\n> Indique o ID do Objeto\n\n0) Cancelar\n===[ OBJETOS DISPONÍVEIS ]===\n"+listaObjetos);
        Objeto objeto = ObjetoService.get(mObjeto);
        while (objeto == null){
            if(mObjeto == 0) return;
            mObjeto = Entrada.leiaInt("===========================[ Object Manager | Manutenções ]===========================\nCadastro (1/3)\n> Objeto informado é inválido!!\n> Indique o ID do Objeto\n\n0) Cancelar\n===[ OBJETOS DISPONÍVEIS ]===\n"+listaObjetos);
            objeto = ObjetoService.get(mObjeto);
        }
                    
        // LER DESCRICAO       
        String mDescricao = Entrada.leiaString("===========================[ Object Manager | Manutenções ]===========================\nCadastro (2/3)\n> Indique a Descrição da Ocorrência\n");
        mDescricao = mDescricao.toUpperCase(); //RS001

        // CONFIRMAR       
        String confirmText = "===========================[ Object Manager | Manutenções ]===========================\n"+
        "Cadastro (3/3)"+
        "\n> Confirme os Dados:"+
        "\n- Objeto: #"+mObjeto+" ("+objeto.getNome()+")"+
        "\n- Descrição: "+mDescricao;

        boolean confirm = Entrada.leiaBoolean(confirmText,"Confirmar","Cancelar");
        if(confirm){

            if ( objeto.isAtivo() && !ManutencaoService.inService(mObjeto) && EmprestimoService.isAvaliable(mObjeto) ){

                int statusCode = ManutencaoService.cadastro(mObjeto, mDescricao);
                String message = "Erro não especificado.";

                switch(statusCode){
                    case 0:
                        message = "A Manutenção foi cadastrada com sucesso!";
                        break;
                    case 1:
                        message = "Não são permitidos campos vazios!";
                        break;
                    default:
                        message = "Erro no banco de dados!";
                        break;
                }

                Entrada.leiaBoolean(message,"OK","Fechar");
                
            }else{
                Entrada.leiaBoolean("O Objeto indicado não se encontra disponível.","OK","Fechar");
            }
        }          
    }
    
    private static Manutencao ConsultarManutencao(boolean showInfo) {
        Manutencao targetManut;
        
        int tId = Entrada.leiaInt("===========================[ Object Manager | Manutenções ]===========================\n[ Buscar ]\n> Indique o ID da Manutenção\n");
        targetManut = ManutencaoService.get(tId);
        
        if(targetManut != null){
            if(showInfo){
                
                String nomeObj = "("+String.valueOf(targetManut.getObjeto())+")";
                Objeto checkObjeto = ObjetoService.get(targetManut.getObjeto());
                if(checkObjeto != null){ nomeObj = nomeObj+" "+checkObjeto.getNome(); }

                String infoText = "===========================[ Object Manager | Manutenções ]===========================\n"+
                "[ Consultar ]"+
                "\n> Dados da Manutenção:"+
                "\n- ID: #"+targetManut.getId()+
                "\n- Objeto: "+nomeObj+
                "\n- Descrição: "+targetManut.getDescricao()+
                "\n- Estado: "+targetManut.statusToString()+
                "\n- Data Entrada: "+targetManut.getDataEntrada()+
                "\n- Data Saida: "+targetManut.getDataSaida();
                Entrada.leiaBoolean(infoText,"OK","Fechar");
            }    
        }else{
            Entrada.leiaBoolean("Manutenção não encontrada com os parâmetros informados!","OK","Fechar");
        }
        
        return targetManut;
    }
    
    private static void AlterarManutencao(){
        Manutencao targetManut = ConsultarManutencao(false);
        
        if(targetManut != null){
            
            String nomeObj = "("+String.valueOf(targetManut.getObjeto())+")";
            Objeto checkObjeto = ObjetoService.get(targetManut.getObjeto());
            if(checkObjeto != null){ nomeObj = nomeObj+" "+checkObjeto.getNome(); }
            
            String alterText = "===========================[ Object Manager | Manutenções ]===========================\n"+
            "[ Alterações ]"+
            "\n> Dados da Manutenção:"+
            "\n- ID: #"+targetManut.getId()+
            "\n- Objeto: "+nomeObj+
            "\n- Descrição: "+targetManut.getDescricao()+
            "\n- Estado: "+targetManut.statusToString()+
            "\n- Data Entrada: "+targetManut.getDataEntrada()+
            "\n- Data Saida: "+targetManut.getDataSaida()+
            "\n============================="+
            "\n> Indique o que deseja alterar:"+
            "\n1) Descrição"+
            "\n2) Estado"+
            "\n\n0) Cancelar";

            boolean exitLoop = false;
            int manutId = targetManut.getId();

            while (!exitLoop){        
                int option = Entrada.leiaInt(alterText);

                switch (option) {
                    case 0:
                        exitLoop = true;
                        break;
                    case 1:
                        String nDesc = Entrada.leiaString("===========================[ Object Manager | Manutenções ]===========================\n[ Alterações ]\n> Indique a Descrição da Ocorrência\n",targetManut.getDescricao());
                        nDesc = nDesc.toUpperCase(); //RS001    
                        if(!nDesc.isBlank()){
                            String confirmDescText = "===========================[ Object Manager | Manutenções ]===========================\n"+
                            "[ Alterações ]"+
                            "\n> Confirme os Dados:"+
                            "\n- ID: #"+manutId+
                            "\n- Descrição: "+nDesc;
                            
                            boolean confirmDesc = Entrada.leiaBoolean(confirmDescText,"Confirmar","Cancelar");
                            if(confirmDesc){
                                exitLoop = true;
                                if ( ManutencaoService.update(manutId, "descricao", nDesc) ){
                                    Entrada.leiaBoolean("Manutenção atualizada com sucesso!","OK","Fechar");
                                }else{
                                    Entrada.leiaBoolean("Não foi possível atualizar os dados da Manutenção!","OK","Fechar");
                                }
                            }
                        }else{
                            Entrada.leiaBoolean("O campo não pode estar vazio!","OK","Fechar");
                        }
                        break;
                        
                    case 2:
                        String alterEstado = "===========================[ Object Manager | Manutenções ]===========================\n"+
                        "[ Alterações ]"+
                        "\n> Indique o Estado Manutenção"+
                        "\n0) Concluído (Atualiza Data de Saída)"+
                        "\n1) Em Serviço"+
                        "\n2) Recebido";
                        
                        int nEstado = -1;
                        while (nEstado < 0 || nEstado > 2){
                            nEstado = Entrada.leiaInt(alterEstado);
                        }
                        
                        String nEstadoStr = "";
                        switch(nEstado){
                            case 0:
                                nEstadoStr = "Concluído (Data de Saída será atualizada!)";
                                break;
                            case 1:
                                nEstadoStr = "Em Serviço";
                                break;
                            case 2:
                                nEstadoStr = "Recebido";
                                break;
                        }

                        String confirmEstadoText = "===========================[ Object Manager | Manutenções ]===========================\n"+
                        "[ Alterações ]"+
                        "\n> Confirme os Dados:"+
                        "\n- ID: #"+manutId+
                        "\n- Estado: "+nEstadoStr;

                        boolean confirmEstado = Entrada.leiaBoolean(confirmEstadoText,"Confirmar","Cancelar");
                        if(confirmEstado){
                            exitLoop = true;
                            if ( ManutencaoService.update(manutId, "status", nEstado) ){
                                if (nEstado == 0){
                                    ManutencaoService.updateDate(manutId, "data_saida");
                                }
                                Entrada.leiaBoolean("Manutenção atualizada com sucesso!","OK","Fechar");
                            }else{
                                Entrada.leiaBoolean("Não foi possível atualizar os dados da Manutenção!","OK","Fechar");
                            }
                        }
                }
            }
        }
    }
    
    private static void ExcluirManutencao(){
        Manutencao targetManut = ConsultarManutencao(false); 
        if(targetManut != null){
            int manutId = targetManut.getId();
            
            String nomeObj = "("+String.valueOf(targetManut.getObjeto())+")";
            Objeto checkObjeto = ObjetoService.get(targetManut.getObjeto());
            if(checkObjeto != null){ nomeObj = nomeObj+" "+checkObjeto.getNome(); }
            
            String deleteText = "===========================[ Object Manager | Manutenções ]===========================\n"+
            "[ Excluir ]"+
            "\n> Dados da Manutenção:"+
            "\n- ID: #"+manutId+
            "\n- Objeto: "+nomeObj+
            "\n- Descrição: "+targetManut.getDescricao()+
            "\n- Estado: "+targetManut.statusToString()+
            "\n- Data Entrada: "+targetManut.getDataEntrada()+
            "\n- Data Saida: "+targetManut.getDataSaida()+
            "\n============================="+
            "\n> Tem certeza em excluir a Manutenção #"+manutId+" ?";
            
            boolean confirm = Entrada.leiaBoolean(deleteText,"Confirmar","Cancelar");
            if(confirm){
                if (ManutencaoService.delete(manutId)){
                    Entrada.leiaBoolean("Manutenção deletada com sucesso!","OK","Fechar");
                }else{
                    Entrada.leiaBoolean("Não foi possível deletar a Manutenção!","OK","Fechar");
                }

            }
        }
    }
    
    private static void ListarManutencoes() {
        String listaTipos = "===========================[ Object Manager | Manutenções ]===========================\n"+
        "ID | OBJETO | DESCRIÇÃO | ESTADO | ENTRADA | SAIDA\n";
        
        ArrayList<Manutencao> manutencoes = ManutencaoService.getAll();
        
        for (Manutencao manut : manutencoes) {
            
            String nomeObj = "("+String.valueOf(manut.getObjeto())+")";
            Objeto checkObjeto = ObjetoService.get(manut.getObjeto());
            if(checkObjeto != null){ nomeObj = nomeObj+" "+checkObjeto.getNome(); }

            listaTipos += ( "#"+manut.getId()+" | "+nomeObj+" | "+manut.getDescricao()+" | "+manut.statusToString()+" | "+manut.getDataEntrada()+" | "+manut.getDataSaida()+"\r\n" );
        }
        
        Entrada.leiaBoolean(listaTipos,"OK","Fechar"); 
    }
}
