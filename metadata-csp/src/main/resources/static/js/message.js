$(function() {
    getMessageList();
});

var rowStr = '<tr><td>#{addAt}</td><td>#{typeName}</td><td>#{content}</td></tr>';

function getMessageList(){
    $.get(
        "/client/message",
        {},
        function(data) {
            var list = data.list;
            $("#dataBody").empty();
            for(var d in list) {
                var row = rowStr.replace("#{addAt}", list[d].addAt)
                .replace("#{typeName}", list[d].typeName)
                .replace("#{content}", list[d].content);
                $("#dataBody").append(row);
            }
        }
    );
}