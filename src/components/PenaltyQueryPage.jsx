import React, { useState, useRef } from 'react';
import { Button, Typography, Select, MenuItem, CircularProgress, Box, Card, FormControl, InputLabel } from '@mui/material';

function PenaltyQueryPage() {
  const [formData, setFormData] = useState({
    langPreferred: 'en',
  });
  const [recording, setRecording] = useState(false);
  const [transcript, setTranscript] = useState('');
  const [responseAudioUrl, setResponseAudioUrl] = useState('');
  const [loading, setLoading] = useState(false);
  const mediaRecorderRef = useRef(null);
  const audioChunksRef = useRef([]);
  const recognitionRef = useRef(null);

  const startRecording = async () => {
    setTranscript('');
    setResponseAudioUrl('');
    setRecording(true);

    const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
    mediaRecorderRef.current = new MediaRecorder(stream);

    mediaRecorderRef.current.ondataavailable = event => {
      audioChunksRef.current.push(event.data);
    };

    mediaRecorderRef.current.onstop = async () => {
      const audioBlob = new Blob(audioChunksRef.current, { type: 'audio/mp3' });
      audioChunksRef.current = [];
      setLoading(true);

      if (recognitionRef.current) {
        recognitionRef.current.stop();
      }

      const formPayload = new FormData();
      formPayload.append('file', audioBlob, 'query.mp3');

      try {
        const response = await fetch('https://b45247fb6e2e.ngrok-free.app/assistant/ask?driverId=D123', {
          method: 'POST',
          body: formPayload,
        });

        if (!response.ok) throw new Error('Backend error');

        const responseBlob = await response.blob();
        const audioUrl = URL.createObjectURL(responseBlob);
        setResponseAudioUrl(audioUrl);
      } catch (error) {
        console.error('Failed to send audio:', error);
      }

      setLoading(false);
    };

    mediaRecorderRef.current.start();

    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    if (SpeechRecognition) {
      recognitionRef.current = new SpeechRecognition();
      recognitionRef.current.lang =
        formData.langPreferred === 'hi' ? 'hi-IN' :
        formData.langPreferred === 'kn' ? 'kn-IN' :
        'en-US';
      recognitionRef.current.interimResults = true;
      recognitionRef.current.maxAlternatives = 1;

      recognitionRef.current.onresult = (event) => {
        const text = Array.from(event.results)
          .map(result => result[0].transcript)
          .join(' ');

        setTranscript(text);
      };

      recognitionRef.current.onerror = (event) => {
        console.error('Speech recognition error:', event.error);
      };

      recognitionRef.current.start();
    }
  };

  const stopRecording = () => {
    if (mediaRecorderRef.current) {
      mediaRecorderRef.current.stop();
    }

    if (recognitionRef.current) {
      recognitionRef.current.stop();
    }

    setRecording(false);
  };

  const handleLanguageChange = (e) => {
    setFormData({ langPreferred: e.target.value });
    setTranscript('');
    setResponseAudioUrl('');
    setLoading(false);
  };

  return (
    <Box sx={{ maxWidth: 400, margin: '2rem auto', padding: '0 1rem' }}>
      <Card sx={{ p: 3, borderRadius: 3, boxShadow: 4 }}>
        <Typography variant="h5" align="center" gutterBottom>
          Penalty Query Assistant
        </Typography>

        <FormControl fullWidth sx={{ mt: 2 }}>
          <InputLabel>Language</InputLabel>
          <Select
            value={formData.langPreferred}
            onChange={handleLanguageChange}
            label="Language"
          >
            <MenuItem value="en">English</MenuItem>
            <MenuItem value="hi">हिन्दी</MenuItem>
            <MenuItem value="kn">ಕನ್ನಡ</MenuItem>
          </Select>
        </FormControl>

        <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, mt: 3 }}>
          {!recording ? (
            <Button variant="contained" color="primary" onClick={startRecording} sx={{ flex: 1 }}>
              Start Recording
            </Button>
          ) : (
            <Button variant="outlined" color="error" onClick={stopRecording} sx={{ flex: 1 }}>
              Stop Recording
            </Button>
          )}
        </Box>

        {loading && (
          <Box sx={{ display: 'flex', justifyContent: 'center', mt: 3 }}>
            <CircularProgress />
          </Box>
        )}

        <Box sx={{ mt: 3 }}>
          <Typography variant="h6">Your Question: </Typography>
          <Typography variant="body1" sx={{ mt: 1, p: 2, backgroundColor: '#f0f0f0', borderRadius: 2, minHeight: '3em' }}>
            {transcript || 'Your spoken words will appear here...'}
          </Typography>
        </Box>

        {responseAudioUrl && (
          <Box sx={{ mt: 3 }}>
            <Typography variant="h6">Porter saathi</Typography>
            <audio controls src={responseAudioUrl} style={{ width: '100%', marginTop: '0.5rem' }} />
          </Box>
        )}
      </Card>
    </Box>
  );
}

export default PenaltyQueryPage;
