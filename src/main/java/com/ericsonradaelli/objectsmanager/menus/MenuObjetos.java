package com.ericsonradaelli.objectsmanager.menus;

import java.util.ArrayList;
import com.ericsonradaelli.objectsmanager.helpers.Entrada;
import com.ericsonradaelli.objectsmanager.entities.Objeto;
import com.ericsonradaelli.objectsmanager.entities.TipoObjeto;
import com.ericsonradaelli.objectsmanager.services.ObjetoService;
import com.ericsonradaelli.objectsmanager.services.TipoObjetoService;


/**
 *
 * @author erics
 */
public class MenuObjetos {
    //Completo - Testado
    public static void show() {

        boolean exitMenu = false;

        while (!exitMenu){        
            String menuText = 
                "=============================[ Object Manager | Objetos ]=============================\n"+
                "1) Incluir Objeto\n"+
                "2) Consultar Objeto\n"+
                "3) Alterar Objeto\n"+
                "4) Excluir Objeto\n"+
                "5) Listar Objetos\n"+
                "\n0) Menu Principal";

            int option = Entrada.leiaInt(menuText);

            switch (option) {
                case 0:
                    exitMenu = true;
                    break;
                case 1:
                    CadastrarObjeto();
                    break;
                case 2:
                    ConsultarObjeto(true);
                    break;
                case 3:
                    AlterarObjeto();
                    break;
                case 4:
                    ExcluirObjeto();
                    break;
                case 5:
                    ListarObjetos();
                    break;
            }
            
        }
    }

    private static void CadastrarObjeto() {
        // LER NOME       
        String oNome = Entrada.leiaString("=============================[ Object Manager | Objetos ]=============================\nCadastro (1/4)\n> Indique o Nome do Objeto\n");
        oNome = oNome.toUpperCase(); //RS001
        // LER DESCRICAO       
        String oDescricao = Entrada.leiaString("=============================[ Object Manager | Objetos ]=============================\nCadastro (2/4)\n> Indique a Descrição do Objeto\n");
        oDescricao = oDescricao.toUpperCase(); //RS001
        // EXIBIR TIPOS JUNTO
        String listaTipos = "";
        ArrayList<TipoObjeto> tipos = TipoObjetoService.getAll();
        for (TipoObjeto tipo : tipos) { listaTipos += ( "#"+tipo.getId()+" | "+tipo.getDescricao()+"\r\n" ); }
        // LER TIPO
        int oTipo = Entrada.leiaInt("=============================[ Object Manager | Objetos ]=============================\nCadastro (3/4)\n> Indique o ID do Tipo de Objeto\n\n0) Cancelar\n===[ TIPOS ]===\n"+listaTipos);
        TipoObjeto tipoObj = TipoObjetoService.get(oTipo);
        while (tipoObj == null){
            if(oTipo == 0) return;
            oTipo = Entrada.leiaInt("=============================[ Object Manager | Objetos ]=============================\nCadastro (3/4)\n> Tipo informado é inválido!!\n> Indique o ID do Tipo de Objeto\n\n0) Cancelar\n===[ TIPOS ]===\n"+listaTipos);
            tipoObj = TipoObjetoService.get(oTipo);
        }
        // CONFIRMAR       
        String confirmText = "=============================[ Object Manager | Objetos ]=============================\n"+
        "Cadastro (4/4)"+
        "\n> Confirme os Dados:"+
        "\n- Nome: "+oNome+
        "\n- Descrição: "+oDescricao+
        "\n- Tipo: #"+oTipo+" ("+tipoObj.getDescricao()+")";

        boolean confirm = Entrada.leiaBoolean(confirmText,"Confirmar","Cancelar");
        if(confirm){
           
            int statusCode = ObjetoService.cadastro(oNome, oDescricao, oTipo);
            String message = "Erro não especificado.";
            
            switch(statusCode){
                case 0:
                    message = "O Objeto foi cadastrado com sucesso!";
                    break;
                case 1:
                    message = "Não são permitidos campos vazios!";
                    break;
                default:
                    message = "Erro no banco de dados!";
                    break;
            }
            
            Entrada.leiaBoolean(message,"OK","Fechar");
        }          
    }
    
    private static Objeto ConsultarObjeto(boolean showInfo) {
        Objeto targetObjeto;
        
        int tId = Entrada.leiaInt("=============================[ Object Manager | Objetos ]=============================\n[ Buscar ]\n> Indique o ID do Objeto\n");
        targetObjeto = ObjetoService.get(tId);
        
        if(targetObjeto != null){
            if(showInfo){
                String estado = "Ativo";
                if (!targetObjeto.isAtivo()){ estado = "Baixado"; }
                
                String descTipo = "("+String.valueOf(targetObjeto.getTipo())+")";
                TipoObjeto checkTipo = TipoObjetoService.get(targetObjeto.getTipo());
                if(checkTipo != null){ descTipo = descTipo+" "+checkTipo.getDescricao(); }
                
                String infoText = "=============================[ Object Manager | Objetos ]=============================\n"+
                "[ Consultar ]"+
                "\n> Dados do Objeto:"+
                "\n- ID: #"+targetObjeto.getId()+
                "\n- Nome: "+targetObjeto.getNome()+
                "\n- Descrição: "+targetObjeto.getDescricao()+
                "\n- Tipo: "+descTipo+
                "\n- Estado: "+estado;
                Entrada.leiaBoolean(infoText,"OK","Fechar");
            }    
        }else{
            Entrada.leiaBoolean("Objeto não encontrado com os parâmetros informados!","OK","Fechar");
        }
        
        return targetObjeto;
    }
    
    private static void AlterarObjeto(){
        Objeto targetObjeto = ConsultarObjeto(false);
        
        if(targetObjeto != null){
            
            String estado = "Ativo";
            if (!targetObjeto.isAtivo()){ estado = "Baixado"; }
            
            String descTipo = "("+String.valueOf(targetObjeto.getTipo()+")");
            TipoObjeto checkTipo = TipoObjetoService.get(targetObjeto.getTipo());
            if(checkTipo != null){ descTipo = descTipo+" "+checkTipo.getDescricao(); }
            
            String alterText = "=============================[ Object Manager | Objetos ]=============================\n"+
            "[ Alterações ]"+
            "\n> Dados do Objeto:"+
            "\n- ID: #"+targetObjeto.getId()+
            "\n- Nome: "+targetObjeto.getNome()+
            "\n- Descrição: "+targetObjeto.getDescricao()+
            "\n- Tipo: "+descTipo+
            "\n- Estado: "+estado+
            "\n============================="+
            "\n> Indique o que deseja alterar:"+
            "\n1) Nome"+
            "\n2) Descrição"+
            "\n3) Tipo"+
            "\n4) Estado"+
            "\n\n0) Cancelar";

            boolean exitLoop = false;
            int objetoId = targetObjeto.getId();

            while (!exitLoop){        
                int option = Entrada.leiaInt(alterText);

                switch (option) {
                    case 0:
                        exitLoop = true;
                        break;
                    case 1: 
                        String nNome = Entrada.leiaString("=============================[ Object Manager | Objetos ]=============================\n[ Alterações ]\n> Indique o Nome do Objeto\n",targetObjeto.getNome());
                        nNome = nNome.toUpperCase(); //RS001    
                        if(!nNome.isBlank()){
                            String confirmNomeText = "=============================[ Object Manager | Objetos ]=============================\n"+
                            "[ Alterações ]"+
                            "\n> Confirme os Dados:"+
                            "\n- ID: #"+objetoId+
                            "\n- Nome: "+nNome;
                            
                            boolean confirmNome = Entrada.leiaBoolean(confirmNomeText,"Confirmar","Cancelar");
                            if(confirmNome){
                                exitLoop = true;
                                if ( ObjetoService.update(objetoId, "nome", nNome) ){
                                    Entrada.leiaBoolean("Dado do Objeto atualizado com sucesso!","OK","Fechar");
                                }else{
                                    Entrada.leiaBoolean("Não foi possível atualizar os dados do Objeto!","OK","Fechar");
                                }
                            }
                        }else{
                            Entrada.leiaBoolean("O campo não pode estar vazio!","OK","Fechar");
                        }
                        break;
                        
                    case 2: 
                        String nDesc = Entrada.leiaString("=============================[ Object Manager | Objetos ]=============================\n[ Alterações ]\n> Indique a Descrição do Objeto\n",targetObjeto.getDescricao());
                        nDesc = nDesc.toUpperCase(); //RS001    
                        if(!nDesc.isBlank()){
                            String confirmDescText = "=============================[ Object Manager | Objetos ]=============================\n"+
                            "[ Alterações ]"+
                            "\n> Confirme os Dados:"+
                            "\n- ID: #"+objetoId+
                            "\n- Descrição: "+nDesc;
                            
                            boolean confirmDesc = Entrada.leiaBoolean(confirmDescText,"Confirmar","Cancelar");
                            if(confirmDesc){
                                exitLoop = true;
                                if ( ObjetoService.update(objetoId, "descricao", nDesc) ){
                                    Entrada.leiaBoolean("Dado do Objeto atualizado com sucesso!","OK","Fechar");
                                }else{
                                    Entrada.leiaBoolean("Não foi possível atualizar os dados do Objeto!","OK","Fechar");
                                }
                            }
                        }else{
                            Entrada.leiaBoolean("O campo não pode estar vazio!","OK","Fechar");
                        }
                        break;
                        
                    case 3:
                        // EXIBIR TIPOS JUNTO
                        String listaTipos = "";
                        ArrayList<TipoObjeto> tipos = TipoObjetoService.getAll();
                        for (TipoObjeto tipo : tipos) { listaTipos += ( "#"+tipo.getId()+" | "+tipo.getDescricao()+"\r\n" ); }
                        // LER TIPO
                        int nTipo = Entrada.leiaInt("=============================[ Object Manager | Objetos ]=============================\n[ Alterações ]\n> Indique o ID do Tipo de Objeto\n\n0) Cancelar\n===[ TIPOS ]===\n"+listaTipos, targetObjeto.getTipo());
                        TipoObjeto tipoObj = TipoObjetoService.get(nTipo);
                        while (tipoObj == null){
                            if(nTipo == 0) return;
                            nTipo = Entrada.leiaInt("=============================[ Object Manager | Objetos ]=============================\n[ Alterações ]\n> Tipo informado é inválido!!\n> Indique o ID do Tipo de Objeto\n\n0) Cancelar\n===[ TIPOS ]===\n"+listaTipos);
                            tipoObj = TipoObjetoService.get(nTipo);
                        }
                             
                        String confirmTipoText = "=============================[ Object Manager | Objetos ]=============================\n"+
                        "[ Alterações ]"+
                        "\n> Confirme os Dados:"+
                        "\n- ID: #"+objetoId+
                        "\n- Tipo: #"+nTipo+" ("+tipoObj.getDescricao()+")";

                        boolean confirmTipo = Entrada.leiaBoolean(confirmTipoText,"Confirmar","Cancelar");
                        if(confirmTipo){
                            exitLoop = true;
                            if ( ObjetoService.update(objetoId, "tipo_id", nTipo) ){
                                Entrada.leiaBoolean("Dado do Objeto atualizado com sucesso!","OK","Fechar");
                            }else{
                                Entrada.leiaBoolean("Não foi possível atualizar os dados do Objeto!","OK","Fechar");
                            }
                        }
                        break;
                    case 4:
                        boolean nState = Entrada.leiaBoolean("=============================[ Object Manager | Objetos ]=============================\n[ Alterações ]\n> Indique o Estado do Objeto\n","Ativo","Baixado");
                        
                        String nEstado = "Ativo";
                        int nEstadoInt = 1;
                        if (!nState){ nEstado = "Baixado"; nEstadoInt = 0; }
            
                        String confirmEstadoText = "=============================[ Object Manager | Objetos ]=============================\n"+
                        "[ Alterações ]"+
                        "\n> Confirme os Dados:"+
                        "\n- ID: #"+objetoId+
                        "\n- Estado: "+nEstado;

                        boolean confirmEstado = Entrada.leiaBoolean(confirmEstadoText,"Confirmar","Cancelar");
                        if(confirmEstado){
                            exitLoop = true;
                            if ( ObjetoService.update(objetoId, "ativo", nEstadoInt) ){
                                Entrada.leiaBoolean("Dado do Objeto atualizado com sucesso!","OK","Fechar");
                            }else{
                                Entrada.leiaBoolean("Não foi possível atualizar os dados do Objeto!","OK","Fechar");
                            }
                        }

                }

            }
        }
    }
    
    private static void ExcluirObjeto(){
        Objeto targetObjeto = ConsultarObjeto(false); 
        if(targetObjeto != null){
            int objetoId = targetObjeto.getId();
            
            String estado = "Ativo";
            if (!targetObjeto.isAtivo()){ estado = "Baixado"; }
           
            String descTipo = "("+String.valueOf(targetObjeto.getTipo())+")";
            TipoObjeto checkTipo = TipoObjetoService.get(targetObjeto.getTipo());
            if(checkTipo != null){ descTipo = descTipo+" "+checkTipo.getDescricao(); }
            
            String deleteText = "=============================[ Object Manager | Objetos ]=============================\n"+
            "[ Excluir ]"+
            "\n> Dados do Objeto:"+
            "\n- ID: #"+objetoId+
            "\n- Nome: "+targetObjeto.getNome()+
            "\n- Descrição: "+targetObjeto.getDescricao()+
            "\n- Tipo: "+descTipo+
            "\n- Estado: "+estado+
            "\n============================="+
            "\n> Tem certeza em excluir o Objeto #"+objetoId+" ?\n> TODOS os dados relacionados (manutenções/empréstimos) serão apagados!";
            
            boolean confirm = Entrada.leiaBoolean(deleteText,"Confirmar","Cancelar");
            if(confirm){
                if (ObjetoService.delete(objetoId)){
                    Entrada.leiaBoolean("Objeto deletado com sucesso!","OK","Fechar");
                }else{
                    Entrada.leiaBoolean("Não foi possível deletar o Objeto!","OK","Fechar");
                }

            }
        }
    }
    
    private static void ListarObjetos() {
        String listaTipos = "=============================[ Object Manager | Objetos ]=============================\n"+
        "ID | NOME | DESCRIÇÃO | TIPO | ESTADO\n";
        
        ArrayList<Objeto> objetos = ObjetoService.getAll();

        for (Objeto objeto : objetos) {
            
            String estado = "Ativo";
            if (!objeto.isAtivo()){ estado = "Baixado"; }
            
            String descTipo = "("+String.valueOf(objeto.getTipo())+")";
            TipoObjeto checkTipo = TipoObjetoService.get(objeto.getTipo());
            if(checkTipo != null){ descTipo += " "+checkTipo.getDescricao(); }
            
            listaTipos += ( "#"+objeto.getId()+" | "+objeto.getNome()+" | "+objeto.getDescricao()+" | "+descTipo+" | "+estado+"\r\n" );
        }
        
        Entrada.leiaBoolean(listaTipos,"OK","Fechar"); 
    }
}