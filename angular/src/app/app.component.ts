import { AfterContentChecked, ChangeDetectorRef, Component, inject } from '@angular/core';
import { AjaxLoadingService } from './services/ajax-loading.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  standalone: false,
  styleUrl: './app.component.css'
})
export class AppComponent implements AfterContentChecked {
  title = 'studentManagement';

  ajaxLoadingService = inject(AjaxLoadingService);

  constructor(private cdr: ChangeDetectorRef) {
  }

  ngAfterContentChecked(): void {
    this.cdr.detectChanges();
  }

}
