import { Component, OnInit } from '@angular/core';
import { Persona } from './persona';
import { PersonaService } from './persona.service';
import {Router, ActivatedRoute} from '@angular/router';
import swal from 'sweetalert2';
@Component({
  selector: 'app-form',
  templateUrl: './form.component.html'
})
export class FormComponent implements OnInit {
  public persona: Persona = new Persona();
  public titulo = 'Crear Persona';
  public errores: string[];

  constructor(private personaService: PersonaService, private router: Router, private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.cargarPersona()
  }

  cargarPersona(): void{
    this.activatedRoute.params.subscribe(params => {
      let id = params['id']
      if(id){
        this.personaService.getPersona(id).subscribe( (persona) => this.persona = persona)
      }
    })
  }

   create(): void {
  this.personaService.create(this.persona)
  .subscribe(persona => {this.router.navigate(['/persona'])
     swal.fire('Persona Registrada', `La persona ${persona.nombre} se ha registrado con exito`, 'success');
     },
     err =>{
       this.errores = err.error.errors as string[];
       console.error('Código de error: ' + err.status);
       console.error(err.error.errors);
     }
    );
  }

  update(): void{
    this.personaService.update(this.persona).subscribe( json => {
      this.router.navigate(['/persona'])
      swal.fire('Persona Actualizada', `La persona ${json.persona.nombre} ha sido actualizada con exito`, 'success')
    },
      err => {
        this.errores = err.error.errors as string[];
        console.error('Código de error: ' + err.status);
        console.error(err.error.errors);
      }
    )
  }

}
