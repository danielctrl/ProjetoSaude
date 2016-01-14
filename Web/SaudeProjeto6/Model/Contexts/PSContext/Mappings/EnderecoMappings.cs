using System.Data.Entity.ModelConfiguration;
using Model.Contexts.PSContext.Model;

namespace Model.Contexts.PSContext.Mappings
{
    class EnderecoMappings : EntityTypeConfiguration<Endereco>
    {
        public EnderecoMappings()
        {
            //Props
            Property(x => x.Id);
            Property(x => x.Cep);
            Property(x => x.Estado);
            Property(x => x.Cidade);
            Property(x => x.Bairro);
            Property(x => x.Logradouro);
            Property(x => x.Numero);
            Property(x => x.Complemento);

            //PK
            HasKey(x => x.Id);

            //FKs
            HasOptional(x => x.Usuario).WithMany(x => x.Enderecos);
            HasOptional(x => x.Paciente).WithMany(x => x.Enderecos);
            HasOptional(x => x.Clinica).WithMany(x => x.Enderecos);
        }
    }
}
