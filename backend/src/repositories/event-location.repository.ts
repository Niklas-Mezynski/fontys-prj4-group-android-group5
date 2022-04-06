import {inject, Getter} from '@loopback/core';
import {DefaultCrudRepository, repository, BelongsToAccessor} from '@loopback/repository';
import {LocalDbDataSource} from '../datasources';
import {EventLocation, EventLocationRelations, Event} from '../models';
import {EventRepository} from './event.repository';

export class EventLocationRepository extends DefaultCrudRepository<
  EventLocation,
  typeof EventLocation.prototype.event_id,
  EventLocationRelations
> {

  public readonly event: BelongsToAccessor<Event, typeof EventLocation.prototype.event_id>;

  constructor(
    @inject('datasources.localDB') dataSource: LocalDbDataSource, @repository.getter('EventRepository') protected eventRepositoryGetter: Getter<EventRepository>,
  ) {
    super(EventLocation, dataSource);
    this.event = this.createBelongsToAccessorFor('event', eventRepositoryGetter,);
    this.registerInclusionResolver('event', this.event.inclusionResolver);
  }
}
