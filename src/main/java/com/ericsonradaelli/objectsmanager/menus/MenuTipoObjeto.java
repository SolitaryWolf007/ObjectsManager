package com.ericsonradaelli.objectsmanager.menus;

import com.ericsonradaelli.objectsmanager.entities.TipoObjeto;
import java.util.ArrayList;
import com.ericsonradaelli.objectsmanager.helpers.Entrada;
import com.ericsonradaelli.objectsmanager.services.TipoObjetoService;


public class MenuTipoObjeto {
    //Completo - Testado
    public static void show() {

        boolean exitMenu = false;

        while (!exitMenu){        
            String menuText = 
                "=========================[ Object Manager | Tipos de Objeto ]=========================\n"+
                "1) Incluir Tipo\n"+
                "2) Consultar Tipo\n"+
                "3) Alterar Tipo\n"+
                "4) Excluir Tipo\n"+
                "5) Listar Tipos\n"+
                "\n0) Menu Principal";

            int option = Entrada.leiaInt(menuText);

            switch (option) {
                case 0:
                    exitMenu = true;
                    break;
                case 1:
                    CadastrarTipoObjeto();
                    break;
                case 2:
                    ConsultarTipoObjeto(true);
                    break;
                case 3:
                    AlterarTipoObjeto();
                    break;
                case 4:
                    ExcluirTipoObjeto();
                    break;
                case 5:
                    ListarTipoObjeto();
                    break;
            }
            
        }
    }

    private static void CadastrarTipoObjeto() {
        String toDescricao = Entrada.leiaString("=========================[ Object Manager | Tipos de Objeto ]=========================\nCadastro (1/2)\n> Indique a descrição do Tipo\n");
        toDescricao = toDescricao.toUpperCase();
       
        String confirmText = "=========================[ Object Manager | Tipos de Objeto ]=========================\n"+
        "Cadastro (2/2)"+
        "\n> Confirme os Dados:"+
        "\n- Descrição: "+toDescricao;

        boolean confirm = Entrada.leiaBoolean(confirmText,"Confirmar","Cancelar");
        if(confirm){
           
            int statusCode = TipoObjetoService.cadastro(toDescricao);
            String message = "Erro não especificado.";
            
            switch(statusCode){
                case 0:
                    message = "O Tipo de Objeto foi cadastrado com sucesso!";
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

    private static TipoObjeto ConsultarTipoObjeto(boolean showInfo) {
        TipoObjeto targetTipo;
        
        int tId = Entrada.leiaInt("=========================[ Object Manager | Tipos de Objeto ]=========================\n[ Buscar ]\n> Indique o ID do Tipo\n");
        targetTipo = TipoObjetoService.get(tId);
        
        if(targetTipo != null){
            if(showInfo){
                String infoText = "=========================[ Object Manager | Tipos de Objeto ]=========================\n"+
                "[ Consultar ]"+
                "\n> Dados do Tipo de Objeto:"+
                "\n- ID: #"+targetTipo.getId()+
                "\n- Descrição: "+targetTipo.getDescricao();
                Entrada.leiaBoolean(infoText,"OK","Fechar");
            }    
        }else{
            Entrada.leiaBoolean("Tipo não encontrado com os parâmetros informados!","OK","Fechar");
        }
        
        return targetTipo;
    }

    private static void AlterarTipoObjeto() {
        TipoObjeto targetTipo = ConsultarTipoObjeto(false);
        
        if(targetTipo != null){
            String alterText = "=========================[ Object Manager | Tipos de Objeto ]=========================\n"+
            "[ Alterações ]"+
            "\n> Dados do Tipo de Objeto:"+
            "\n- ID: #"+targetTipo.getId()+
            "\n- Descrição: "+targetTipo.getDescricao()+
            "\n============================="+
            "\n> Indique o que deseja alterar:"+
            "\n1) Descrição"+
            "\n\n0) Cancelar";

            boolean exitLoop = false;
            int tipoId = targetTipo.getId();

            while (!exitLoop){        
                int option = Entrada.leiaInt(alterText);

                switch (option) {
                    case 0:
                        exitLoop = true;
                        break;
                    case 1: 
                        String nDesc = Entrada.leiaString("=========================[ Object Manager | Tipos de Objeto ]=========================\n[ Alterações ]\n> Indique a Descrição do Tipo\n",targetTipo.getDescricao());
                        nDesc = nDesc.toUpperCase(); //RS001    
                        if(!nDesc.isBlank()){
                            String confirmText = "=========================[ Object Manager | Tipos de Objeto ]=========================\n"+
                            "[ Alterações ]"+
                            "\n> Confirme os Dados:"+
                            "\n- ID: #"+tipoId+
                            "\n- Descrição: "+nDesc;
                            
                            boolean confirm = Entrada.leiaBoolean(confirmText,"Confirmar","Cancelar");
                            if(confirm){
                                exitLoop = true;
                                if ( TipoObjetoService.update(tipoId, "descricao", nDesc) ){
                                    Entrada.leiaBoolean("Dado do Tipo de Objeto atualizado com sucesso!","OK","Fechar");
                                }else{
                                    Entrada.leiaBoolean("Não foi possível atualizar os dados do Tipo!","OK","Fechar");
                                }
                            }
                        }else{
                            Entrada.leiaBoolean("O campo não pode estar vazio!","OK","Fechar");
                        }
                        break;
                    
                }

            }
        }
    }

    private static void ExcluirTipoObjeto() {
        TipoObjeto targetTipo = ConsultarTipoObjeto(false); 
        if(targetTipo != null){
            int tipoId = targetTipo.getId();
            String deleteText = "=========================[ Object Manager | Tipos de Objeto ]=========================\n"+
            "[ Excluir ]"+
            "\n> Dados do Tipo de Objeto:"+
            "\n- ID: #"+tipoId+
            "\n- Descrição: "+targetTipo.getDescricao()+
            "\n============================="+
            "\n> Tem certeza em excluir o Tipo #"+tipoId+" ?";
            
            boolean confirm = Entrada.leiaBoolean(deleteText,"Confirmar","Cancelar");
            if(confirm){
                if (TipoObjetoService.delete(tipoId)){
                    Entrada.leiaBoolean("Tipo deletado com sucesso!","OK","Fechar");
                }else{
                    Entrada.leiaBoolean("Não foi possível deletar o Tipo!","OK","Fechar");
                }

            }
        }
    }

    private static void ListarTipoObjeto() {
        String listaTipos = "=========================[ Object Manager | Tipos de Objeto ]=========================\n"+
        "ID | DESCRIÇÃO\n";
        
        ArrayList<TipoObjeto> tipos = TipoObjetoService.getAll();

        for (TipoObjeto tipo : tipos) {
            listaTipos += ( "#"+tipo.getId()+" | "+tipo.getDescricao()+"\r\n" );
        }
        
        Entrada.leiaBoolean(listaTipos,"OK","Fechar"); 
    }
  
}
