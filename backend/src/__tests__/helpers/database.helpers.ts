import { Getter } from '@loopback/core';
import {
    EventLocationRepository,
    EventRepository,
    FriendsRepository,
    PicturesRepository,
    TicketRequestRepository,
    TicketRepository,
    UserLocationRepository,
    UserRepository
} from '../../repositories';
import { testdb } from '../fixtures/datasources/testdb.datasource';

export async function givenEmptyDatabase() {
    let eventLocationRepository: EventLocationRepository;
    let eventRepository: EventRepository;
    let friendsRepository: FriendsRepository;
    let picturesRepository: PicturesRepository;
    let ticketRequestRepository: TicketRequestRepository;
    let ticketRepository: TicketRepository;
    let userLocationRepository: UserLocationRepository;
    let userRepository: UserRepository;

    eventLocationRepository = new EventLocationRepository(
        testdb,
        async () => eventRepository
    );

    eventRepository = new EventRepository(
        testdb,
        async () => picturesRepository,
        async () => eventLocationRepository,
        async () => ticketRepository,
        async () => userRepository,
        async () => ticketRequestRepository,
    );

    friendsRepository = new FriendsRepository(
        testdb,
    );

    picturesRepository = new PicturesRepository(
        testdb,
        async () => eventRepository
    )

    ticketRequestRepository = new TicketRequestRepository(
        testdb,
        async () => userRepository,
        async () => eventRepository
    )

    ticketRepository = new TicketRepository(
        testdb,
        async () => userRepository,
        async () => eventRepository
    )

    userLocationRepository = new UserLocationRepository(
        testdb,
        async () => userRepository,
    )

    userRepository = new UserRepository(
        testdb,
        async () => userLocationRepository,
        async () => ticketRepository,
        async () => eventRepository,
        async () => ticketRequestRepository,
    )


    await eventLocationRepository.deleteAll();
    await eventRepository.deleteAll();
    await friendsRepository.deleteAll();
    await picturesRepository.deleteAll();
    await ticketRequestRepository.deleteAll();
    await ticketRepository.deleteAll();
    await userLocationRepository.deleteAll();
    await userRepository.deleteAll();

    return testdb;
}