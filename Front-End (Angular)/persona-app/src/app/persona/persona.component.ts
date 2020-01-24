import { Component, OnInit } from '@angular/core';
import { Persona } from './persona';
import { PersonaService } from './persona.service';
import { ModalService } from './detalle/modal.service';
import Swal from 'sweetalert2';
import {tap} from 'rxjs/operators';
import {ActivatedRoute} from '@angular/router';


@Component({
  selector: 'app-persona',
  templateUrl: './persona.component.html'
})
export class PersonaComponent implements OnInit {

 persona: Persona[];
 paginador: any;
 personaSeleccionada: Persona;
  constructor(private personaService: PersonaService,
    private modalService: ModalService,
    private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.activatedRoute.paramMap.subscribe(params => {
            let page: number = +params.get('page');

            if (!page) {
                page = 0;
            }

            this.personaService.getPersonas(page).pipe(tap(response => {
                    console.log('PersonaComponent: tap 3');
                    (response.content as Persona[]).forEach(persona => {
                        console.log(persona.nombre);
                    });
                }))
                .subscribe(response => {
                  this.persona = response.content as Persona[];
                  this.paginador = response;
                });
        });

        this.modalService.notificarUpload.subscribe(persona=>{
          this.persona = this.persona.map(personaOriginal =>{
            if(persona.id == personaOriginal.id){
              personaOriginal.foto = persona.foto;
            }
            return personaOriginal;
          })
        })
}

  delete(persona: Persona): void {
    const swalWithBootstrapButtons = Swal.mixin({
      customClass: {
        confirmButton: 'btn btn-success',
        cancelButton: 'btn btn-danger'
      },
      buttonsStyling: false
    })

    swalWithBootstrapButtons.fire({
      title: '¿Eliminar?',
      text: `¿Seguro que desea eliminar a la persona ${persona.nombre} ${persona.apellidos}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Si,Eliminar',
      cancelButtonText: 'No,Cancelar',
      reverseButtons: true
    }).then((result) => {
      if (result.value) {
        this.personaService.delete(persona.id).subscribe(
          response => {
            this.persona = this.persona.filter(per => per !== persona)
            swalWithBootstrapButtons.fire(
              'Eliminado',
              `La persona ${persona.nombre} ha sido eliminada con exito`,
              'success'
            )
          }
        )
      }
    })
  }

  abrirModal(persona: Persona){
    this.personaSeleccionada = persona;
    this.modalService.abrirModal();
  }

}
