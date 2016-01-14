namespace Model.Contexts.PSContext.Model
{
    class Telefone
    {
        //Props simples
        public int Id { get; set; }
        public string Numero { get; set; }
        public int TipoTelefone { get; set; }

        //Fks
        //public int? UsuarioId { get; set; }
        //public int? ClinicaId { get; set; }
        //public int? PacienteId { get; set; }

        //Props complexas
        public Usuario Usuario { get; set; }
        public Clinica Clinica { get; set; }
        public Paciente Paciente { get; set; }
    }
}
