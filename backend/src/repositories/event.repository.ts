import {inject, Getter} from '@loopback/core';
import {DefaultCrudRepository, repository, HasManyRepositoryFactory, HasOneRepositoryFactory} from '@loopback/repository';
import {LocalDbDataSource} from '../datasources';
import {Event, EventRelations, Pictures, EventLocation, Ticket} from '../models';
import {PicturesRepository} from './pictures.repository';
import {EventLocationRepository} from './event-location.repository';
import {TicketRepository} from './ticket.repository';

export class EventRepository extends DefaultCrudRepository<
  Event,
  typeof Event.prototype.id,
  EventRelations
> {

  public readonly pictures: HasManyRepositoryFactory<Pictures, typeof Event.prototype.id>;

  public readonly eventLocation: HasOneRepositoryFactory<EventLocation, typeof Event.prototype.id>;

  public readonly tickets: HasManyRepositoryFactory<Ticket, typeof Event.prototype.id>;

  constructor(
    @inject('datasources.localDB') dataSource: LocalDbDataSource, @repository.getter('PicturesRepository') protected picturesRepositoryGetter: Getter<PicturesRepository>, @repository.getter('EventLocationRepository') protected eventLocationRepositoryGetter: Getter<EventLocationRepository>, @repository.getter('TicketRepository') protected ticketRepositoryGetter: Getter<TicketRepository>,
  ) {
    super(Event, dataSource);
    this.tickets = this.createHasManyRepositoryFactoryFor('tickets', ticketRepositoryGetter,);
    this.registerInclusionResolver('tickets', this.tickets.inclusionResolver);
    this.eventLocation = this.createHasOneRepositoryFactoryFor('eventLocation', eventLocationRepositoryGetter);
    this.registerInclusionResolver('eventLocation', this.eventLocation.inclusionResolver);
    this.pictures = this.createHasManyRepositoryFactoryFor('pictures', picturesRepositoryGetter,);
    this.registerInclusionResolver('pictures', this.pictures.inclusionResolver);
  }
}
