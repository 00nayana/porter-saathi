import React, { useState } from 'react';
import { Box, Tabs, Tab, Typography, Link, Paper, Button } from '@mui/material';
import App from './App';

const TabPanel = ({ children, value, index }) => {
  return (
    <div role="tabpanel" hidden={value !== index}>
      {value === index && (
        <Box sx={{ p: 3 }}>
          <Typography>{children}</Typography>
        </Box>
      )}
    </div>
  );
};

const InfoTabs = () => {
  const [tabIndex, setTabIndex] = useState(0);

  const handleTabChange = (event, newValue) => {
    setTabIndex(newValue);
  };

  const teamMembers = [
    {
      name: 'Nayana Murthy',
      pic: '/image31.jpg',  // Place these in the public folder
      resume: '/NAYANA_RESUME25.pdf',
    },
    {
      name: 'Vasavi Agarwal',
      pic: '/vasavi.png.jpg',
      resume: '/Vasavi_Resume.pdf',
    },
  ];

  return (
    <Box sx={{ maxWidth: 800, margin: '2rem auto', padding: '1rem' }}>
      <Paper elevation={3}>
        <Tabs
          value={tabIndex}
          onChange={handleTabChange}
          indicatorColor="primary"
          textColor="primary"
          variant="fullWidth"
        >
          <Tab label="App Link" />
          <Tab label="Problem Statement" />
          <Tab label="Architecture" />
          <Tab label="Team Members" />
        </Tabs>

        <TabPanel value={tabIndex} index={0}>
          <App />
        </TabPanel>

        <TabPanel value={tabIndex} index={1}>
      
          <Button
            variant="contained"
            component="a"
            href="https://uc.hackerearth.com/he-public-ap-south-1/Porter%20Chhalaang_%20Problem%20Statement%2014th%20August48027f9.pdf"
            target="_blank"
            download
          >
            Download PDF


            
          </Button>
          <div >
  <h2>Problem Statement</h2>
  <p><strong>The "Porter Saathi": An AI Voice-First Partner for Empowerment and Accessibility</strong></p>
  <p><strong>Theme:</strong> Bridging the Literacy Gap with Conversational AI</p>

  <h3>Context:</h3>
  <p>A significant portion of our driver-partners are ambitious and entrepreneurial but may not have high levels of formal education. This literacy barrier presents a major, often overlooked, challenge in their daily operations and long-term growth. What might seem simple to some is a significant hurdle for them. They find it incredibly difficult to read and to draw logical conclusions and learnings from text-based information, which leads to numerous frustrations:</p>

  <ul>
    <li><strong>App Interaction:</strong> Simple tasks like understanding form validation errors, comprehending why a penalty was applied or a reward was given, or navigating text-heavy support flows become deeply challenging and can lead to a difficult onboarding experience.</li>
    <li><strong>Financial Growth:</strong> Their inability to easily read and understand financial statements, loan documents, or business performance metrics hinders their entrepreneurial journey.</li>
    <li><strong>Access to External Systems:</strong> They struggle to leverage the broader support ecosystem. Important services like applying for gig worker insurance, contesting traffic challans (tickets), or using government platforms like DigiLocker remain inaccessible because they are complex and text-reliant.</li>
  </ul>

  <p>This creates a dependency on others and prevents them from being truly independent and empowered.</p>

  <h3>The Challenge:</h3>
  <p>Your challenge is to design and build a prototype of the "Porter Saathi" – a voice-first, vernacular-driven AI assistant integrated within the Porter app. This "Saathi" (Partner) will be the primary interface for the driver, translating complex, text-based information into simple, actionable voice and visual conversations. The goal is to make the digital world accessible and manageable for a less-literate user, empowering them to run their business, stay safe, and upskill with confidence.</p>

  <h3>The solution should focus on features across three core areas, using AI to translate text and process into intuitive conversation:</h3>

  <h4>1. The Simplified Business Manager:</h4>
  <ul>
    <li><strong>Conversational Finance:</strong> Instead of showing text-based earnings statements, the Saathi should be able to answer questions like, "Aaj ka kharcha kaat ke kitna kamaya?" (How much did I earn today after expenses?). It should also explain penalties or rewards using simple voice notes and visuals, for instance, "This penalty was applied because the delivery was late by 30 minutes."</li>
    <li><strong>Voice-Led Onboarding & Forms:</strong> Guide users through any form-filling process (like onboarding or document submission) using voice prompts, explaining each field, and helping them correct errors conversationally, rather than showing red error text.</li>
    <li><strong>Business Growth Simplified:</strong> Explain business metrics through voice. For example, the driver could ask, "Mera business pichle hafte se behtar hai ya nahi?" (Is my business better than last week?), and the AI should provide a simple, spoken summary.</li>
  </ul>

  <h4>2. The Accessible "Guru" for Life Skills:</h4>
  <ul>
    <li><strong>Navigating the Ecosystem:</strong> Create AI-powered guides that walk a driver through complex external processes. For example, a step-by-step, voice-led tutorial on "How to contest a challan online" or "How to upload documents to DigiLocker."</li>
    <li><strong>On-Demand Learning:</strong> Provide a library of short, audio/visual learning modules on critical topics like understanding vehicle insurance, basics of customer service, or information on government schemes for gig workers, all accessible through simple voice search.</li>
    <li><strong>Interactive Problem Solving:</strong> When a driver faces an issue, instead of making them read a text-based FAQ, the Saathi should engage in a diagnostic conversation to understand the problem and provide a clear, spoken solution.</li>
  </ul>

  <h4>3. The Intuitive "Suraksha" Shield for Safety:</h4>
  <ul>
    <li><strong>Zero-Reading Emergency Assistance:</strong> Reinforce the one-touch "Sahayata" (Help) button that relies purely on voice interaction. In an emergency, a driver under stress should not be expected to read instructions. The AI must handle the entire triage and response process through a calm, conversational flow.</li>
    <li><strong>Audio Safety Alerts:</strong> Instead of text pop-ups, use voice alerts for potentially unsafe routes or weather conditions. For example, "Aage-sadak kharab hai, kripya dhyaan se chalayein." (The road ahead is bad, please drive carefully).</li>
  </ul>

  <h3>AI Component:</h3>
  <p>The core of this solution is a robust, empathetic AI engine. This requires state-of-the-art regional language NLP and Speech-to-Text/Text-to-Speech, an AI layer capable of translating structured data (like earnings reports or error codes) into simple, natural language, and a decision-making framework that can guide users through complex workflows conversationally.</p>
</div>

        </TabPanel>

        <TabPanel value={tabIndex} index={2}>
          <img
            src="/archi.png"
            alt="Architecture Diagram"
            style={{ maxWidth: '100%', marginTop: '1rem' }}
          />
        </TabPanel>

        <TabPanel value={tabIndex} index={3}>
        <Box sx={{ mt: 3 }}>
      <Typography variant="h6">Team Members</Typography>

      {teamMembers.map((member, idx) => (
        <Paper
          key={idx}
          sx={{ display: 'flex', alignItems: 'center', padding: '1rem', marginTop: '1rem' }}
        >
          <img
            src={member.pic}
            alt={member.name}
            style={{ width: '100px', height: '100px', borderRadius: '50%', marginRight: '1rem' }}
          />
          <Box>
            <Typography variant="subtitle1" fontWeight="bold">
              {member.name}
            </Typography>
            <a href={member.resume} download>
              <Button variant="outlined" sx={{ mt: 1 }}>
                Download Resume
              </Button>
            </a>
          </Box>
        </Paper>
      ))}
    </Box>
        </TabPanel>
      </Paper>
    </Box>
  );
};

export default InfoTabs;
