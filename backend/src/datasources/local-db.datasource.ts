import {inject, lifeCycleObserver, LifeCycleObserver} from '@loopback/core';
import {juggler} from '@loopback/repository';

const config = {
  name: 'localDB',
  connector: 'postgresql',
  url: '',
  host: 'localhost',
  port: 5432,
  user: 'postgres',
  password: 'prj4Group5',
  database: 'postgres'
};

// Observe application's life cycle to disconnect the datasource when
// application is stopped. This allows the application to be shut down
// gracefully. The `stop()` method is inherited from `juggler.DataSource`.
// Learn more at https://loopback.io/doc/en/lb4/Life-cycle.html
@lifeCycleObserver('datasource')
export class LocalDbDataSource extends juggler.DataSource
  implements LifeCycleObserver {
  static dataSourceName = 'localDB';
  static readonly defaultConfig = config;

  constructor(
    @inject('datasources.config.localDB', {optional: true})
    dsConfig: object = config,
  ) {
    super(dsConfig);
  }
}
