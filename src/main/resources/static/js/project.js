$(document).ready(function() {
  // alert("js bonjour ok");

});


$( "#form-id").focusin(function() {
    //le bouton est activé si on se trouve sur le formulaire
    $('#btnAjout').attr('disabled',false);
});


$( "#form-id").focus(function() {
    //le bouton est activé si on se trouve sur le formulaire
    $('#btnAjout').attr('disabled',false);
});

$("#btn-raz").click(function(evenement) {
    evenement.preventDefault();
    //alert("recu 0")
    $("#form-id input" ).val(null);
    $('#btnAjout').attr('disabled',true);
});

function delete() {
    confirm("Voulez-vous vraiment supprimer?");
}