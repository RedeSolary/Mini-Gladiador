package br.kaiosouza.minigladiador.models;

import br.kaiosouza.minigladiador.type.ParameterType;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class Parameter {

    private ParameterType parameterType;
    private String parameter;
    private List<String> parameters;

    public Parameter(String parameter) {
        this.parameterType = ParameterType.ONE;
        this.parameter = parameter;
    }

    public Parameter(String... parameters) {
        this.parameterType = ParameterType.LIST;
        this.parameters = Arrays.asList(parameters);
    }

    public String getDisplay(){
        StringBuilder commandHelp = new StringBuilder();
        if(parameterType == ParameterType.ONE){
            commandHelp.append(" <").append(parameter).append(">");
        }else{
            commandHelp.append(" <");
            for(int i = 0 ; i < parameters.size() ; i++){
                commandHelp.append("§a").append(parameters.get(i));
                if(i < parameters.size() - 1) commandHelp.append("§7, ");
            }
            commandHelp.append("§a>");
        }
        return commandHelp.toString();
    }

}
