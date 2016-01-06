using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(SaudeProjeto6.Startup))]
namespace SaudeProjeto6
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
