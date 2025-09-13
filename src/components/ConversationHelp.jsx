import React, { useState } from 'react';
import {
  Box,
  Typography,
  Button,
  CircularProgress,
  Paper,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from '@mui/material';
import MicIcon from '@mui/icons-material/Mic';
import StopIcon from '@mui/icons-material/Stop';
import axios from 'axios';

const ConversationHelp = () => {
  const [recording, setRecording] = useState(false);
  const [mediaRecorder, setMediaRecorder] = useState(null);
  const [audioChunks, setAudioChunks] = useState([]);
  const [loading, setLoading] = useState(false);
  const [responseAudio, setResponseAudio] = useState(null);
  const [language, setLanguage] = useState('english');

  const startRecording = async () => {
    setResponseAudio(null);
    setRecording(true);
    const stream = await navigator.mediaDevices.getUserMedia({ audio: true });
    const recorder = new MediaRecorder(stream);
    setMediaRecorder(recorder);
    const chunks = [];

    recorder.ondataavailable = (e) => chunks.push(e.data);
    recorder.onstop = async () => {
      const blob = new Blob(chunks, { type: 'audio/webm' });
      setAudioChunks(chunks);
      sendAudio(blob);
    };

    recorder.start();
  };

  const stopRecording = () => {
    setRecording(false);
    if (mediaRecorder) {
      mediaRecorder.stop();
    }
  };

  const sendAudio = async (audioBlob) => {
    setLoading(true);
    try {
      const formData = new FormData();
      formData.append('file', audioBlob, 'user_speech.webm');

      const response = await axios.post(
        `https://b45247fb6e2e.ngrok-free.app/conversation?language=${language}`,
        formData,
        { headers: { 'Content-Type': 'multipart/form-data' }, responseType: 'blob' }
      );

      const audioUrl = URL.createObjectURL(response.data);
      setResponseAudio(audioUrl);

      const audio = new Audio(audioUrl);
      audio.play().catch(() => {
        console.warn('User interaction required to play audio.');
      });
    } catch (error) {
      console.error('Error sending audio:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box
      sx={{
        maxWidth: 600,
        margin: '2rem auto',
        padding: '2rem',
        borderRadius: 3,
        boxShadow: 4,
        backgroundColor: '#f5f5f5',
        textAlign: 'center',
      }}
    >
      <Typography variant="h4" fontWeight="bold" gutterBottom>
        Conversation Help
      </Typography>

      <Paper elevation={3} sx={{ padding: '1rem', marginBottom: '1rem' }}>
        <Typography variant="body1" gutterBottom>
          Select a language, press the mic button, and speak. The system will help you converse.
        </Typography>

        <FormControl fullWidth sx={{ mb: 2 }}>
          <InputLabel id="language-label">Language</InputLabel>
          <Select
            labelId="language-label"
            value={language}
            label="Language"
            onChange={(e) => setLanguage(e.target.value)}
          >
            <MenuItem value="english">English</MenuItem>
            <MenuItem value="hindi">Hindi</MenuItem>
            <MenuItem value="kannada">Kannada</MenuItem>
            <MenuItem value="tamil">Tamil</MenuItem>
            <MenuItem value="telugu">Telugu</MenuItem>
            {/* Add more languages as needed */}
          </Select>
        </FormControl>

        <Button
          variant="contained"
          color={recording ? 'error' : 'primary'}
          onClick={recording ? stopRecording : startRecording}
          startIcon={recording ? <StopIcon /> : <MicIcon />}
          sx={{ mt: 2 }}
        >
          {recording ? 'Stop Recording' : 'Start Speaking'}
        </Button>

        {loading && (
          <Box sx={{ mt: 2 }}>
            <CircularProgress />
            <Typography variant="body2">Processing your voice...</Typography>
          </Box>
        )}
      </Paper>

      {responseAudio && (
        <Box sx={{ mt: 2 }}>
          <Typography variant="h6">Response Audio:</Typography>
          <audio controls src={responseAudio} />
        </Box>
      )}
    </Box>
  );
};

export default ConversationHelp;
