namespace Model.Contexts.PSContext.Model
{
    class Endereco
    {
        //Props simples
        public int Id { get; set; }
        public string Cep { get; set; }
        public string Estado { get; set; }
        public string Cidade { get; set; }
        public string Bairro { get; set; }
        public string Logradouro { get; set; }
        public string Numero { get; set; }
        public string Complemento { get; set; }

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






