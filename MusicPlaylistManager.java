import java.sql.*;
import java.util.Scanner;

public class MusicPlaylistManager {
    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/music_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    // Method to establish database connection
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Add a song to the database
    private static void addSong(String title, String artist, String genre) {
        String query = "INSERT INTO songs (title, artist, genre) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, title);
            stmt.setString(2, artist);
            stmt.setString(3, genre);
            stmt.executeUpdate();
            System.out.println("Song added successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding song: " + e.getMessage());
        }
    }

    // View all songs in the database
    private static void viewSongs() {
        String query = "SELECT * FROM songs";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("Songs:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("song_id") +
                                   ", Title: " + rs.getString("title") +
                                   ", Artist: " + rs.getString("artist") +
                                   ", Genre: " + rs.getString("genre"));
            }
        } catch (SQLException e) {
            System.out.println("Error fetching songs: " + e.getMessage());
        }
    }

    // Create a new playlist
    private static void createPlaylist(String name) {
        String query = "INSERT INTO playlists (name) VALUES (?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            System.out.println("Playlist created successfully.");
        } catch (SQLException e) {
            System.out.println("Error creating playlist: " + e.getMessage());
        }
    }

    // Add a song to a playlist
    private static void addSongToPlaylist(int playlistId, int songId) {
        String query = "INSERT INTO playlist_songs (playlist_id, song_id) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, playlistId);
            stmt.setInt(2, songId);
            stmt.executeUpdate();
            System.out.println("Song added to playlist successfully.");
        } catch (SQLException e) {
            System.out.println("Error adding song to playlist: " + e.getMessage());
        }
    }

    // View all songs in a playlist
    private static void viewPlaylist(int playlistId) {
        String query = "SELECT s.title, s.artist, s.genre " +
                       "FROM songs s " +
                       "JOIN playlist_songs ps ON s.song_id = ps.song_id " +
                       "WHERE ps.playlist_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, playlistId);
            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("Playlist Songs:");
                while (rs.next()) {
                    System.out.println("Title: " + rs.getString("title") +
                                       ", Artist: " + rs.getString("artist") +
                                       ", Genre: " + rs.getString("genre"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error viewing playlist: " + e.getMessage());
        }
    }

    // Main method for user interaction
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMusic Playlist Management System");
            System.out.println("1. Add Song");
            System.out.println("2. View Songs");
            System.out.println("3. Create Playlist");
            System.out.println("4. Add Song to Playlist");
            System.out.println("5. View Playlist");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter song title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter artist: ");
                    String artist = scanner.nextLine();
                    System.out.print("Enter genre: ");
                    String genre = scanner.nextLine();
                    addSong(title, artist, genre);
                    break;
                case 2:
                    viewSongs();
                    break;
                case 3:
                    System.out.print("Enter playlist name: ");
                    String playlistName = scanner.nextLine();
                    createPlaylist(playlistName);
                    break;
                case 4:
                    System.out.print("Enter playlist ID: ");
                    int playlistId = scanner.nextInt();
                    System.out.print("Enter song ID: ");
                    int songId = scanner.nextInt();
                    addSongToPlaylist(playlistId, songId);
                    break;
                case 5:
                    System.out.print("Enter playlist ID: ");
                    int plId = scanner.nextInt();
                    viewPlaylist(plId);
                    break;
                case 6:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
