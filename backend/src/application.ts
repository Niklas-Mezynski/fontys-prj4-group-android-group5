import { BootMixin } from '@loopback/boot';
import { ApplicationConfig } from '@loopback/core';
import {
  RestExplorerBindings,
  RestExplorerComponent,
} from '@loopback/rest-explorer';
import { RepositoryMixin } from '@loopback/repository';
import { RestApplication } from '@loopback/rest';
import { ServiceMixin } from '@loopback/service-proxy';
import path from 'path';
import { MySequence } from './sequence';
import { AuthenticationComponent } from '@loopback/authentication';
import {
  JWTAuthenticationComponent,
  SECURITY_SCHEME_SPEC,
  UserServiceBindings,
} from '@loopback/authentication-jwt';
import { LocalDbDataSource } from './datasources';
import { UserRepository } from './repositories';
import { CustomUserService } from './services';
import { UserService } from '@loopback/authentication';
import { applicationDefault, cert, Credential, initializeApp } from "firebase-admin/app";

export { ApplicationConfig };

// const firebasePath = "C:\\Users\\nikla\\Documents\\Uni\\Semester_4\\PRJ4\\projectx_admin_sdk.json";
const firebasePath = "..\\..\\projectx_admin_sdk.json";

// Initialize firebase-admin app
// export const firebaseApp = initializeApp({ credential: cert(firebasePath) });
export const firebaseApp = initializeApp({ credential: applicationDefault() });

export class ProjectX extends BootMixin(
  ServiceMixin(RepositoryMixin(RestApplication)),
) {
  constructor(options: ApplicationConfig = {}) {
    super(options);

    // Set up the custom sequence
    this.sequence(MySequence);

    // Set up default home page
    this.static('/', path.join(__dirname, '../public'));

    // Customize @loopback/rest-explorer configuration here
    this.configure(RestExplorerBindings.COMPONENT).to({
      path: '/explorer',
    });
    this.component(RestExplorerComponent);

    this.projectRoot = __dirname;
    // Customize @loopback/boot Booter Conventions here
    this.bootOptions = {
      controllers: {
        // Customize ControllerBooter Conventions here
        dirs: ['controllers'],
        extensions: ['.controller.js'],
        nested: true,
      },
    };

    this.component(AuthenticationComponent);
    // Mount jwt component
    this.component(JWTAuthenticationComponent);
    // Bind user and credentials repository
    this.bind(UserServiceBindings.USER_REPOSITORY).toClass(
      UserRepository,
    );
    // Bind datasource
    this.bind(UserServiceBindings.USER_SERVICE).toClass(CustomUserService);

    this.dataSource(LocalDbDataSource, UserServiceBindings.DATASOURCE_NAME);

  }
}
