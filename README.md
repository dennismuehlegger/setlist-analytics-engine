# Setlist Analytics Engine
Spring Boot REST API modeling a concert setlist data processing pipeline. 
The system runs automated background workers to get tour data from the 
Setlist.fm API, processes nested JSON structures, and exposes calculated 
performance metrics.

---

## Current Features

### Artists & Setlists
- Fetch real-time concert setlist history from Setlist.fm by MusicBrainz Identifier (MBID)
- Raw external JSON parsing and processing using Jackson
- Flattens complex, deeply-nested concert data structures into unified, clean JSON outputs

---

## Planned Features
- **Data Persistence** – Save processed setlist records to database to prevent hitting API rate limits
- **Automated Background Workers** – Spring `@Scheduled` tasks to auto-fetch new tour data overnight
- **Performance Metrics** – Analytics endpoints calculating most-played songs, top opener/closer frequencies, and average setlist lengths

---

## Architecture
- **Controller layer** – REST endpoints receiving path variables (MBIDs)
- **Service layer** – Business logic managing external client requests and payload processing
- **Repository layer** - Database access
- **DTO layer** – Simple representation objects optimizing front-end payload delivery
- **Database** – H2 (in-memory, for local development)

---

## How to Run

### 1. Set your API Key Environment Variable
The application requires your Setlist.fm API key to be set in your system environment (e.g. in your IntelliJ Run Configuration):
```env
SETLISTFM_API_KEY=your_actual_setlist_fm_api_key_here
