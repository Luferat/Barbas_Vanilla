$(document).ready(()=>{
    $(document).on('click', '#changePhoto', (ev)=>{
        ev.preventDefault();
        $('#photoDialog')[0].showModal();
        return false;
    });

    $(document).on('click', '#closeDialog', (ev=>{
        $('#photoDialog')[0].close();
    }));
});