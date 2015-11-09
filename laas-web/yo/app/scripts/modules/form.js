LaaS.module('Form', function(Form, LaaS, Backbone, Marionette) {
    'use strict';

    Form.getDropDownSelected = function(value, target_value) {
        return (value && target_value && value.toLowerCase() == target_value.toLowerCase()) ? "selected='selected'" : "";
    };

    Form.generateParameterSubForm = function(parameterDefines, parameters) {
        var subHtml = "";
        var lineContained = 2;
        var elementInLine = 0;
        for (var i=0; parameterDefines && i<parameterDefines.length; i++) {
            if (elementInLine == 0) {
                subHtml += '<div class="two fields">';
            }

            if (elementInLine < lineContained) {
                subHtml += '<div class="field">';
                subHtml += getSubFormField(parameterDefines[i], parameters);
                subHtml += '</div>';
                if (parameterDefines[i]["lineOccupied"]  || (parameterDefines[i+1] && parameterDefines[i+1]["lineOccupied"])) {
                    elementInLine = lineContained;
                }
            }
            elementInLine++;
            if (elementInLine >= lineContained) {
                subHtml += '</div>';
                elementInLine = 0;
            }
        }

        if (elementInLine > 0) {
            subHtml += '</div>';
        }
        return subHtml;
    };

    var getSubFormField = function(parameterDefine, parameters) {
        var fieldHtml = "";
        fieldHtml += '<label>'+parameterDefine["displayInfo"]+'</label>';
        switch(parameterDefine.type) {
            case "time":
                fieldHtml += '<input id="datetimepicker" type="text" />';
                //var value = parameters[parameterDefine["name"]] || parameterDefine["defaultValue"] || '';
                //fieldHtml += '<input id="datetimepicker" type="text" name="'
                //    +parameterDefine["name"]+'" value="'+ value +'"/>';
                break;
            case "text":
                fieldHtml += '<input type="text" name="'+parameterDefine["name"]
                    +'" placeholder="'+(parameterDefine["defaultValue"] || '')
                    +'" value="'+(parameters[parameterDefine["name"]] || parameterDefine["defaultValue"] || '')
                    +'"/>';
                break;
            case "dropdown":
                fieldHtml += '<select name="'+parameterDefine["name"]+'", class="ui dropdown">';
                var valueList = parameterDefine["valueList"] ? parameterDefine["valueList"].split("|") : [];
                var displayList = parameterDefine["displayList"] ? parameterDefine["displayList"].split("|") : [];
                for (var i=0; i<valueList.length; i++) {
                    fieldHtml += '<option value="'+valueList[i]+'" '+LaaS.Form.getDropDownSelected(valueList[i], parameters[parameterDefine["name"]] || parameterDefine["defaultValue"])+'>';
                    fieldHtml += displayList[i] || valueList[i];
                    fieldHtml += '</option>';
                }
                fieldHtml += "</select>";
                break;
            default:
                break;
        }
        return fieldHtml;
    }
});