LaaS.module('Form', function(Form, LaaS, Backbone, Marionette) {
    'use strict';

    Form.getDropDownSelected = function(value, target_value) {
        return (value && target_value && value.toLowerCase() == target_value.toLowerCase()) ? "selected='selected'" : "";
    };

    Form.generateParameterSubForm = function(parameterDefines, parameters, readonly) {
        var subHtml = "";
        var lineContained = 2;
        var elementInLine = 0;
        for (var i=0; parameterDefines && i<parameterDefines.length; i++) {
            if (elementInLine == 0) {
                subHtml += '<div class="two fields">';
            }

            if (elementInLine < lineContained) {
                subHtml += '<div class="field">';
                subHtml += getSubFormField(parameterDefines[i], parameters, readonly);
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
            if (elementInLine < lineContained) {
                subHtml += '<div class="field"/>';
            }
            subHtml += '</div>';
        }
        return subHtml;
    };

    Form.enableDatetimeControl = function(parameterDefines) {
        for (var i=0; parameterDefines && i<parameterDefines.length; i++) {
            if (parameterDefines[i]["type"] == "time") {
                var controlId = getParameterControlId(parameterDefines[i]);
                $("#"+controlId).datetimepicker({
                    format:"Y/m/d H:i"
                });
            }
        }
    };

    var getParameterControlId = function(parameterDefine) {
        return "Parameter_" + parameterDefine["name"];
    };

    var getSubFormField = function(parameterDefine, parameters, readonly) {
        var fieldHtml = "";
        fieldHtml += '<label>'+parameterDefine["displayInfo"]+'</label>';
        var value = parameters[parameterDefine["name"]] || parameterDefine["defaultValue"] || '';
        var controlId = getParameterControlId(parameterDefine);
        switch(parameterDefine.type) {
            case "time":
                fieldHtml += '<input id="' + controlId + '" type="text" name="'
                    +parameterDefine["name"]+'" value="'+ value +'" ' + (readonly ? 'readonly="true"' : '') + '/>';
                break;
            case "text":
                fieldHtml += '<input id="' + controlId + '" type="text" name="'+parameterDefine["name"]
                    +'" placeholder="'+(parameterDefine["defaultValue"] || '')
                    +'" value="' + value +'" ' + (readonly ? 'readonly="true"' : '') + '/>';
                break;
            case "dropdown":
                fieldHtml += '<select id="' + controlId + '" name="'+parameterDefine["name"]+'", class="ui dropdown" '
                    + (readonly ? 'disabled="disabled"' : '') +'>';
                var valueList = parameterDefine["valueList"] ? parameterDefine["valueList"].split("|") : [];
                var displayList = parameterDefine["displayList"] ? parameterDefine["displayList"].split("|") : [];
                for (var i=0; i<valueList.length; i++) {
                    fieldHtml += '<option value="'+valueList[i]+'" '+LaaS.Form.getDropDownSelected(valueList[i], value)+'>';
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