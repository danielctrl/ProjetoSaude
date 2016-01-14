using System;
using System.Collections.Generic;
using System.Data.Entity.Core.Metadata.Edm;

namespace Model.Contexts.PSContext.Model
{
    class PacienteParentesco
    {
        //Props simples
        public int Id { get; set; }
        public int TipoParentescoAehDeB { get; set; } //Model.Enum.ModelEnum.TipoParentesco
        //public int TipoParentescoBehDeA { get; set; } //Model.Enum.ModelEnum.TipoParentesco
        //#ToDo Validar regra parentesco

        //Fks

        //Props complexas
        public virtual Paciente PacientesFamiliarA { get; set; }
        public virtual Paciente PacientesFamiliarB { get; set; }
    }
}