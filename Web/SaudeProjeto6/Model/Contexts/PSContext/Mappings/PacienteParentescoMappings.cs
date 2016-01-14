using System.Data.Entity.ModelConfiguration;
using Model.Contexts.PSContext.Model;

namespace Model.Contexts.PSContext.Mappings
{
    class PacienteParentescoMappings : EntityTypeConfiguration<PacienteParentesco>
    {
        public PacienteParentescoMappings()
        {
            //
            ToTable("PacienteParentesco");

            //Props
            Property(x => x.Id);
            Property(x => x.TipoParentescoAehDeB);
            //Property(x => x.TipoParentescoBehDeA);

            //PK
            HasKey(x => x.Id);

            //FKs
            HasRequired(x => x.PacientesFamiliarA).WithMany(x => x.FamiliaresA);
            HasRequired(x => x.PacientesFamiliarB).WithMany(x => x.FamiliaresB);
        }
    }
}
