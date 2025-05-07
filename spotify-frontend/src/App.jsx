import { useState } from 'react';

function App() {
  const [isrc, setIsrc] = useState('');
  const [trackInfo, setTrackInfo] = useState(null);
  const [coverImageUrl, setCoverImageUrl] = useState('');
  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState('');

  const username = 'admin';
  const password = 'admin';
  const apiUrl = 'http://localhost:8080/track';

  const handleLookup = async () => {
    setLoading(true);
    setErrorMessage('');
    setTrackInfo(null);
    setCoverImageUrl('');

    try {
      const headers = {
        'Authorization': 'Basic ' + btoa(`${username}:${password}`)
      };

      await fetch(`${apiUrl}/createTrack?isrc=${isrc}`, {
        method: 'POST',
        headers,
        credentials: 'include'
      });

      const metadataResponse = await fetch(`${apiUrl}/getTrackMetadata?isrc=${isrc}`, {
        headers,
        credentials: 'include'
      });
      const metadata = await metadataResponse.json();
      setTrackInfo(metadata);

      const coverResponse = await fetch(`${apiUrl}/getCover?isrc=${isrc}`, {
        headers,
        credentials: 'include'
      });
      const imageBlob = await coverResponse.blob();
      setCoverImageUrl(URL.createObjectURL(imageBlob));

    } catch (e) {
      setErrorMessage('Failed to fetch.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ fontFamily: 'sans-serif', padding: '2rem', maxWidth: '600px', margin: 'auto' }}>
      <h2>Track Finder</h2>
      <input
        type="text"
        value={isrc}
        onChange={(e) => setIsrc(e.target.value)}
        placeholder="Enter ISRC"
        style={{ padding: '0.5rem', width: '100%', marginBottom: '1rem' }}
      />
      <button onClick={handleLookup} disabled={loading || !isrc} style={{ padding: '0.5rem 1rem' }}>
        {loading ? 'Loading...' : 'Search'}
      </button>

      {errorMessage && <p style={{ color: 'red', marginTop: '1rem' }}>{errorMessage}</p>}

      {trackInfo && (
        <div style={{ marginTop: '2rem' }}>
          <h3>{trackInfo.name}</h3>
          <p><strong>Artist:</strong> {trackInfo.artistName}</p>
          <p><strong>Album:</strong> {trackInfo.albumName}</p>
          <p><strong>Explicit:</strong> {trackInfo.explicit ? 'Yes' : 'No'}</p>
          <p><strong>Duration:</strong> {trackInfo.playbackSeconds} seconds</p>
          {coverImageUrl && <img src={coverImageUrl} alt="Album Cover" style={{ maxWidth: '100%', marginTop: '1rem' }} />}
        </div>
      )}
    </div>
  );
}

export default App;
