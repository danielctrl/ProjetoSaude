using System.Data.Entity.ModelConfiguration;
using Model.Contexts.PSContext.Model;

namespace Model.Contexts.PSContext.Mappings
{
    class ConvenioMappings : EntityTypeConfiguration<Convenio>
    {
        public ConvenioMappings()
        {
            //Props
            Property(x => x.Id);
            Property(x => x.Nome);
            Property(x => x.Registro);

            //PK
            HasKey(x => x.Id);

            //FKs
            HasMany(x => x.PacientesConvenio).WithMany(x => x.Convenios);
        }
    }
}
